package nl.wendyarthouse.wa_backend.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotEmpty

data class CreateOrderRequest(
    @field:NotBlank(message = "Naam is verplicht")
    val customerName: String,

    @field:NotBlank(message = "E-mail is verplicht")
    @field:Email(message = "Ongeldig e-mailadres")
    val customerEmail: String,

    @field:NotBlank(message = "Adres is verplicht")
    val shippingAddress: String,

    @field:NotEmpty(message = "Bestelling bevat geen producten")
    val items: List<CartItemDto>,

    @field:NotBlank(message = "Betaalreferentie ontbreekt")
    val stripePaymentIntentId: String,
)
