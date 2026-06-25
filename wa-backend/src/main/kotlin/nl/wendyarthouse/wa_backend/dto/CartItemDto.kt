package nl.wendyarthouse.wa_backend.dto

import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Positive

data class CartItemDto(
    @field:Positive(message = "Ongeldig product-ID")
    val productId: Long,

    @field:Positive(message = "Aantal moet minimaal 1 zijn")
    @field:Max(value = 100, message = "Aantal mag maximaal 100 zijn")
    val quantity: Int,
)
