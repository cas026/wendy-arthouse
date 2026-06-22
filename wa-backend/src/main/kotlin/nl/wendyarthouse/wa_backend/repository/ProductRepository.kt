package nl.wendyarthouse.wa_backend.repository

import nl.wendyarthouse.wa_backend.model.Product
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ProductRepository : JpaRepository<Product, Long> {
    fun findByCategory(category: String): List<Product>
    fun findByNameContainingIgnoreCase(name: String): List<Product>
}