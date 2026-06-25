package nl.wendyarthouse.wa_backend.controller

import jakarta.servlet.http.HttpServletResponse
import nl.wendyarthouse.wa_backend.dto.LoginRequest
import nl.wendyarthouse.wa_backend.security.JwtUtil
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseCookie
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AnonymousAuthenticationToken
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.beans.factory.annotation.Value
import org.springframework.web.bind.annotation.*
import java.time.Duration

@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val authManager: AuthenticationManager,
    private val jwtUtil: JwtUtil,
    @Value("\${server.secure-cookies:false}") private val secureCookies: Boolean,
) {
    @PostMapping("/login")
    fun login(@RequestBody request: LoginRequest, response: HttpServletResponse): ResponseEntity<Map<String, String>> {
        return try {
            authManager.authenticate(
                UsernamePasswordAuthenticationToken(request.username, request.password)
            )
            val token = jwtUtil.generate(request.username)
            response.addHeader(HttpHeaders.SET_COOKIE, buildCookie("admin_token", token, Duration.ofHours(8)))
            ResponseEntity.ok(mapOf("username" to request.username))
        } catch (_: BadCredentialsException) {
            ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(mapOf("error" to "Ongeldige gebruikersnaam of wachtwoord"))
        }
    }

    @PostMapping("/logout")
    fun logout(response: HttpServletResponse): ResponseEntity<Void> {
        response.addHeader(HttpHeaders.SET_COOKIE, buildCookie("admin_token", "", Duration.ZERO))
        return ResponseEntity.noContent().build()
    }

    @GetMapping("/me")
    fun me(): ResponseEntity<Map<String, String>> {
        val auth = SecurityContextHolder.getContext().authentication
        if (auth == null || auth is AnonymousAuthenticationToken || !auth.isAuthenticated) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()
        }
        return ResponseEntity.ok(mapOf("username" to auth.name))
    }

    private fun buildCookie(name: String, value: String, maxAge: Duration): String =
        ResponseCookie.from(name, value)
            .httpOnly(true)
            .secure(secureCookies)
            .sameSite("Lax")
            .path("/")
            .maxAge(maxAge)
            .build()
            .toString()
}
