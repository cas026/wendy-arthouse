package nl.wendyarthouse.wa_backend.service

import com.stripe.exception.StripeException
import com.stripe.model.PaymentIntent
import com.stripe.param.PaymentIntentCreateParams
import nl.wendyarthouse.wa_backend.dto.CartItemDto
import nl.wendyarthouse.wa_backend.dto.CreateOrderRequest
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
        val total = calculateTotal(items)
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

        val order = Order(
            customerName = request.customerName,
            customerEmail = request.customerEmail,
            shippingAddress = request.shippingAddress,
            status = OrderStatus.PAID,
            totalAmount = calculateTotal(request.items),
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

    private fun calculateTotal(items: List<CartItemDto>): BigDecimal =
        items.fold(BigDecimal.ZERO) { sum, dto ->
            val product = productRepository.findById(dto.productId)
                .orElseThrow { NoSuchElementException("Product met id ${dto.productId} niet gevonden") }
            sum + product.price.multiply(dto.quantity.toBigDecimal())
        }
}
