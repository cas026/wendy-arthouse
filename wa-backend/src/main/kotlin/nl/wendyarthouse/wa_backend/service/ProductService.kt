package nl.wendyarthouse.wa_backend.service

import nl.wendyarthouse.wa_backend.dto.ProductRequest
import nl.wendyarthouse.wa_backend.model.Product
import nl.wendyarthouse.wa_backend.repository.ProductRepository
import org.springframework.stereotype.Service

@Service
class ProductService(private val productRepository: ProductRepository) {

    fun getAllProducts(): List<Product> = productRepository.findAll()

    fun getProductById(id: Long): Product =
        productRepository.findById(id)
            .orElseThrow { NoSuchElementException("Product with id $id not found") }

    fun getProductsByCategory(category: String): List<Product> =
        productRepository.findByCategory(category)

    fun searchProducts(name: String): List<Product> =
        productRepository.findByNameContainingIgnoreCase(name)

    fun createProduct(request: ProductRequest): Product =
        productRepository.save(
            Product(
                name = request.name,
                description = request.description,
                price = request.price,
                imageUrl = request.imageUrl,
                category = request.category,
                stock = request.stock,
            )
        )

    fun updateProduct(id: Long, request: ProductRequest): Product {
        val existing = getProductById(id)
        return productRepository.save(
            existing.copy(
                name = request.name,
                description = request.description,
                price = request.price,
                imageUrl = request.imageUrl,
                category = request.category,
                stock = request.stock,
            )
        )
    }

    fun updateImageUrl(id: Long, imageUrl: String): Product {
        val existing = getProductById(id)
        return productRepository.save(existing.copy(imageUrl = imageUrl))
    }

    fun deleteProduct(id: Long) {
        if (!productRepository.existsById(id)) {
            throw NoSuchElementException("Product with id $id not found")
        }
        productRepository.deleteById(id)
    }
}
