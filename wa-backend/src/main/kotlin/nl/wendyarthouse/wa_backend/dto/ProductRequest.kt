package nl.wendyarthouse.wa_backend.dto

import java.math.BigDecimal

data class ProductRequest(
    val name: String,
    val description: String? = null,
    val price: BigDecimal,
    val imageUrl: String? = null,
    val category: String? = null,
    val stock: Int = 0,
)
