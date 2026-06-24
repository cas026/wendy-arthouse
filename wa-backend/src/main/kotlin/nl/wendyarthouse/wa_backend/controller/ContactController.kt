package nl.wendyarthouse.wa_backend.controller

import jakarta.validation.Valid
import nl.wendyarthouse.wa_backend.dto.ContactRequest
import nl.wendyarthouse.wa_backend.service.EmailService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/contact")
class ContactController(private val emailService: EmailService) {

    @PostMapping
    fun sendMessage(@Valid @RequestBody request: ContactRequest): ResponseEntity<Void> {
        emailService.sendContactMessage(request)
        return ResponseEntity.status(HttpStatus.ACCEPTED).build()
    }
}
