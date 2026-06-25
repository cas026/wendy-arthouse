package nl.wendyarthouse.wa_backend.controller

import nl.wendyarthouse.wa_backend.dto.AdminOrderResponse
import nl.wendyarthouse.wa_backend.dto.ProductRequest
import nl.wendyarthouse.wa_backend.dto.UpdateOrderStatusRequest
import nl.wendyarthouse.wa_backend.model.Product
import nl.wendyarthouse.wa_backend.service.OrderService
import nl.wendyarthouse.wa_backend.service.ProductService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.io.File

@RestController
@RequestMapping("/api/admin")
class AdminController(
    private val productService: ProductService,
    private val orderService: OrderService,
) {
    // ─── Products ─────────────────────────────────────────────────────────────

    @PostMapping("/products")
    @ResponseStatus(HttpStatus.CREATED)
    fun createProduct(@Valid @RequestBody request: ProductRequest): Product =
        productService.createProduct(request)

    @PutMapping("/products/{id}")
    fun updateProduct(
        @PathVariable id: Long,
        @Valid @RequestBody request: ProductRequest,
    ): ResponseEntity<Product> =
        try {
            ResponseEntity.ok(productService.updateProduct(id, request))
        } catch (e: NoSuchElementException) {
            ResponseEntity.notFound().build()
        }

    @DeleteMapping("/products/{id}")
    fun deleteProduct(@PathVariable id: Long): ResponseEntity<Void> =
        try {
            productService.deleteProduct(id)
            ResponseEntity.noContent().build()
        } catch (e: NoSuchElementException) {
            ResponseEntity.notFound().build()
        }

    @PostMapping("/products/{id}/image", consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun uploadProductImage(
        @PathVariable id: Long,
        @RequestParam("file") file: MultipartFile,
    ): ResponseEntity<Product> {
        val allowedTypes = mapOf("image/jpeg" to "jpg", "image/png" to "png", "image/webp" to "webp")
        val extension = allowedTypes[file.contentType]
            ?: return ResponseEntity.badRequest().build()
        val filename = "${id}_${System.currentTimeMillis()}.$extension"
        val uploadDir = File("uploads/products").absoluteFile.also { it.mkdirs() }
        file.transferTo(File(uploadDir, filename))
        return try {
            ResponseEntity.ok(productService.updateImageUrl(id, "/uploads/products/$filename"))
        } catch (e: NoSuchElementException) {
            ResponseEntity.notFound().build()
        }
    }

    // ─── Orders ───────────────────────────────────────────────────────────────

    @GetMapping("/orders")
    fun getAllOrders(): List<AdminOrderResponse> = orderService.getAllOrders()

    @PatchMapping("/orders/{id}/status")
    fun updateOrderStatus(
        @PathVariable id: Long,
        @RequestBody request: UpdateOrderStatusRequest,
    ): ResponseEntity<AdminOrderResponse> =
        try {
            ResponseEntity.ok(orderService.updateOrderStatus(id, request))
        } catch (e: NoSuchElementException) {
            ResponseEntity.notFound().build()
        }
}
