package nl.wendyarthouse.wa_backend.dto

import jakarta.validation.constraints.NotEmpty

data class PaymentIntentRequest(
    @field:NotEmpty
    val items: List<CartItemDto>,
)
