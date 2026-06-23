package nl.wendyarthouse.wa_backend.controller

import nl.wendyarthouse.wa_backend.dto.CreateOrderRequest
import nl.wendyarthouse.wa_backend.dto.OrderResponse
import nl.wendyarthouse.wa_backend.dto.PaymentIntentRequest
import nl.wendyarthouse.wa_backend.service.OrderService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import jakarta.validation.Valid

@RestController
@RequestMapping("/api/orders")
class OrderController(private val orderService: OrderService) {

    @PostMapping("/payment-intent")
    fun createPaymentIntent(@Valid @RequestBody request: PaymentIntentRequest): Map<String, String> {
        val clientSecret = orderService.createPaymentIntent(request.items)
        return mapOf("clientSecret" to clientSecret)
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createOrder(@Valid @RequestBody request: CreateOrderRequest): OrderResponse {
        val order = orderService.createOrder(request)
        return OrderResponse(
            id = order.id,
            status = order.status,
            totalAmount = order.totalAmount,
            customerEmail = order.customerEmail,
        )
    }
}
