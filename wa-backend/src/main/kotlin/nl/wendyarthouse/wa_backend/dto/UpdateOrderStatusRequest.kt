package nl.wendyarthouse.wa_backend.dto

import nl.wendyarthouse.wa_backend.model.OrderStatus

data class UpdateOrderStatusRequest(
    val status: OrderStatus,
    val trackingCode: String? = null,
)
