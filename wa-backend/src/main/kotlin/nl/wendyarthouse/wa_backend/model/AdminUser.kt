package nl.wendyarthouse.wa_backend.model

import jakarta.persistence.*

@Entity
@Table(name = "admin_users")
class AdminUser(
    @Column(unique = true, nullable = false) var username: String,
    @Column(nullable = false) var passwordHash: String,
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) var id: Long = 0,
)
