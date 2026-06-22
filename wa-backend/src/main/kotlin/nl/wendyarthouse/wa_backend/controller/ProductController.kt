package nl.wendyarthouse.wa_backend.controller

import nl.wendyarthouse.wa_backend.model.Product
import nl.wendyarthouse.wa_backend.service.ProductService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/products")
class ProductController(
    private val productService: ProductService
) {
    @GetMapping
    fun getAllProducts(
        @RequestParam(required = false) category: String?,
        @RequestParam(required = false) search: String?
    ): List<Product> = when {
        category != null -> productService.getProductsByCategory(category)
        search != null   -> productService.searchProducts(search)
        else             -> productService.getAllProducts()
    }

    @GetMapping("/{id}")
    fun getProductById(@PathVariable id: Long): ResponseEntity<Product> =
        try {
            ResponseEntity.ok(productService.getProductById(id))
        } catch (e: NoSuchElementException) {
            ResponseEntity.notFound().build()
        }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createProduct(@RequestBody product: Product): Product =
        productService.saveProduct(product)

    @DeleteMapping("/{id}")
    fun deleteProduct(@PathVariable id: Long): ResponseEntity<Void> =
        try {
            productService.deleteProduct(id)
            ResponseEntity.noContent().build()
        } catch (e: NoSuchElementException) {
            ResponseEntity.notFound().build()
        }
}