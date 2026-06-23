package nl.wendyarthouse.wa_backend.repository

import nl.wendyarthouse.wa_backend.model.AdminUser
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface AdminUserRepository : JpaRepository<AdminUser, Long> {
    fun findByUsername(username: String): AdminUser?
}
