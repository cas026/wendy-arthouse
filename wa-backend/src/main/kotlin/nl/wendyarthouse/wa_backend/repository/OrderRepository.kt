package nl.wendyarthouse.wa_backend.repository

import nl.wendyarthouse.wa_backend.model.Order
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface OrderRepository : JpaRepository<Order, Long>
