package nl.wendyarthouse.wa_backend.dto

import jakarta.validation.constraints.DecimalMin
import jakarta.validation.constraints.Digits
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import java.math.BigDecimal

data class ProductRequest(
    @field:NotBlank(message = "Naam is verplicht")
    @field:Size(max = 255)
    val name: String,

    @field:Size(max = 5000)
    val description: String? = null,

    @field:NotNull(message = "Prijs is verplicht")
    @field:DecimalMin(value = "0.01", message = "Prijs moet minimaal €0,01 zijn")
    @field:Digits(integer = 8, fraction = 2, message = "Ongeldige prijs")
    val price: BigDecimal,

    @field:Size(max = 500)
    val imageUrl: String? = null,

    @field:Size(max = 100)
    val category: String? = null,

    @field:Min(value = 0, message = "Voorraad kan niet negatief zijn")
    val stock: Int = 0,
)
