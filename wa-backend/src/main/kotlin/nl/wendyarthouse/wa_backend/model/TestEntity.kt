package nl.wendyarthouse.wa_backend.model

import jakarta.persistence.*

@Entity
@Table(name = "test_entity")
data class TestEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    val naam: String = ""
)