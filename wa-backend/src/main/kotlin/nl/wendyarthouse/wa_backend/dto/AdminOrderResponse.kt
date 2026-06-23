package nl.wendyarthouse.wa_backend.dto

import nl.wendyarthouse.wa_backend.model.OrderStatus
import java.math.BigDecimal
import java.time.LocalDateTime

data class AdminOrderItemResponse(
    val productName: String,
    val quantity: Int,
    val priceAtPurchase: BigDecimal,
)

data class AdminOrderResponse(
    val id: Long,
    val customerName: String,
    val customerEmail: String,
    val shippingAddress: String,
    val status: OrderStatus,
    val totalAmount: BigDecimal,
    val createdAt: LocalDateTime,
    val items: List<AdminOrderItemResponse>,
)
