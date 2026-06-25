package nl.wendyarthouse.wa_backend.dto

import jakarta.validation.Valid
import jakarta.validation.constraints.NotEmpty

data class PaymentIntentRequest(
    @field:NotEmpty(message = "Winkelwagen is leeg")
    @field:Valid
    val items: List<CartItemDto>,
)
