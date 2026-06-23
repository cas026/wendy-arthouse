package nl.wendyarthouse.wa_backend.dto

import nl.wendyarthouse.wa_backend.model.OrderStatus
import java.math.BigDecimal

data class OrderResponse(
    val id: Long,
    val status: OrderStatus,
    val totalAmount: BigDecimal,
    val customerEmail: String,
)
