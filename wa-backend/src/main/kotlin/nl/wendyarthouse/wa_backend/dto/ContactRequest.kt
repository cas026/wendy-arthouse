package nl.wendyarthouse.wa_backend.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class ContactRequest(
    @field:NotBlank @field:Size(min = 2, max = 100)
    val name: String,

    @field:NotBlank @field:Email
    val email: String,

    @field:NotBlank @field:Size(min = 10, max = 2000)
    val message: String,
)
