package nl.wendyarthouse.wa_backend.security

import nl.wendyarthouse.wa_backend.repository.AdminUserRepository
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class AdminUserDetailsService(private val adminUserRepository: AdminUserRepository) : UserDetailsService {

    override fun loadUserByUsername(username: String): UserDetails =
        adminUserRepository.findByUsername(username)
            ?.let {
                User.withUsername(it.username)
                    .password(it.passwordHash)
                    .roles("ADMIN")
                    .build()
            }
            ?: throw UsernameNotFoundException("Admin user not found: $username")
}
