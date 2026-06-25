package nl.wendyarthouse.wa_backend.service

import com.stripe.exception.StripeException
import com.stripe.model.PaymentIntent
import com.stripe.param.PaymentIntentCreateParams
import nl.wendyarthouse.wa_backend.dto.AdminOrderItemResponse
import nl.wendyarthouse.wa_backend.dto.AdminOrderResponse
import nl.wendyarthouse.wa_backend.dto.CartItemDto
import nl.wendyarthouse.wa_backend.dto.CreateOrderRequest
import nl.wendyarthouse.wa_backend.dto.UpdateOrderStatusRequest
import nl.wendyarthouse.wa_backend.model.Order
import nl.wendyarthouse.wa_backend.model.OrderItem
import nl.wendyarthouse.wa_backend.model.OrderStatus
import nl.wendyarthouse.wa_backend.repository.OrderRepository
import nl.wendyarthouse.wa_backend.repository.ProductRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal
import java.math.RoundingMode

@Service
class OrderService(
    private val orderRepository: OrderRepository,
    private val productRepository: ProductRepository,
    private val emailService: EmailService,
    @Value("\${stripe.payment-method-configuration}") private val paymentMethodConfigId: String,
) {
    fun createPaymentIntent(items: List<CartItemDto>): String {
        val subtotal = calculateTotal(items)
        val total = subtotal + calculateShipping(subtotal)
        val amountInCents = total.multiply(BigDecimal("100")).setScale(0, RoundingMode.HALF_UP).toLong()

        val params = PaymentIntentCreateParams.builder()
            .setAmount(amountInCents)
            .setCurrency("eur")
            .setPaymentMethodConfiguration(paymentMethodConfigId)
            .build()

        return PaymentIntent.create(params).clientSecret
    }

    @Transactional
    fun createOrder(request: CreateOrderRequest): Order {
        val paymentIntent = try {
            PaymentIntent.retrieve(request.stripePaymentIntentId)
        } catch (e: StripeException) {
            throw IllegalStateException("Kan betaling niet verifiëren: ${e.message}")
        }

        if (paymentIntent.status != "succeeded") {
            throw IllegalStateException("Betaling is nog niet voltooid (status: ${paymentIntent.status})")
        }

        val subtotal = calculateTotal(request.items)
        val expectedCents = (subtotal + calculateShipping(subtotal))
            .multiply(BigDecimal("100")).setScale(0, RoundingMode.HALF_UP).toLong()
        require(paymentIntent.amount == expectedCents) {
            "Betaalbedrag klopt niet: verwacht ${expectedCents}¢, ontvangen ${paymentIntent.amount}¢"
        }
        val order = Order(
            customerName = request.customerName,
            customerEmail = request.customerEmail,
            shippingAddress = request.shippingAddress,
            status = OrderStatus.PAID,
            totalAmount = subtotal + calculateShipping(subtotal),
            stripePaymentIntentId = request.stripePaymentIntentId,
        )

        request.items.forEach { dto ->
            val product = productRepository.findById(dto.productId)
                .orElseThrow { NoSuchElementException("Product met id ${dto.productId} niet gevonden") }
            order.items.add(
                OrderItem(
                    order = order,
                    productName = product.name,
                    priceAtPurchase = product.price,
                    quantity = dto.quantity,
                )
            )
        }

        val savedOrder = orderRepository.save(order)
        emailService.sendOrderConfirmation(savedOrder)
        return savedOrder
    }

    fun getAllOrders(): List<AdminOrderResponse> =
        orderRepository.findAllByOrderByCreatedAtDesc().map { order ->
            AdminOrderResponse(
                id = order.id,
                customerName = order.customerName,
                customerEmail = order.customerEmail,
                shippingAddress = order.shippingAddress,
                status = order.status,
                totalAmount = order.totalAmount,
                createdAt = order.createdAt,
                trackingCode = order.trackingCode,
                items = order.items.map { item ->
                    AdminOrderItemResponse(
                        productName = item.productName,
                        quantity = item.quantity,
                        priceAtPurchase = item.priceAtPurchase,
                    )
                },
            )
        }

    @Transactional
    fun updateOrderStatus(id: Long, request: UpdateOrderStatusRequest): AdminOrderResponse {
        val order = orderRepository.findById(id)
            .orElseThrow { NoSuchElementException("Order with id $id not found") }
        order.status = request.status
        if (request.status == OrderStatus.SHIPPED && request.trackingCode != null) {
            order.trackingCode = request.trackingCode
        }
        val saved = orderRepository.save(order)
        if (request.status == OrderStatus.SHIPPED) {
            emailService.sendShippingConfirmation(saved)
        }
        return AdminOrderResponse(
            id = saved.id,
            customerName = saved.customerName,
            customerEmail = saved.customerEmail,
            shippingAddress = saved.shippingAddress,
            status = saved.status,
            totalAmount = saved.totalAmount,
            createdAt = saved.createdAt,
            trackingCode = saved.trackingCode,
            items = saved.items.map { item ->
                AdminOrderItemResponse(item.productName, item.quantity, item.priceAtPurchase)
            },
        )
    }

    private fun calculateTotal(items: List<CartItemDto>): BigDecimal =
        items.fold(BigDecimal.ZERO) { sum, dto ->
            val product = productRepository.findById(dto.productId)
                .orElseThrow { NoSuchElementException("Product met id ${dto.productId} niet gevonden") }
            sum + product.price.multiply(dto.quantity.toBigDecimal())
        }

    private fun calculateShipping(subtotal: BigDecimal): BigDecimal =
        if (subtotal >= BigDecimal("75.00")) BigDecimal.ZERO else BigDecimal("4.95")
}
