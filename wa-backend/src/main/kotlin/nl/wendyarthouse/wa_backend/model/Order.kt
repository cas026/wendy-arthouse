package nl.wendyarthouse.wa_backend.model

import jakarta.persistence.*
import java.math.BigDecimal
import java.time.LocalDateTime

@Entity
@Table(name = "orders")
class Order(
    @Column(nullable = false)
    var customerName: String,

    @Column(nullable = false)
    var customerEmail: String,

    @Column(nullable = false, columnDefinition = "TEXT")
    var shippingAddress: String,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var status: OrderStatus = OrderStatus.PENDING_PAYMENT,

    @Column(nullable = false, precision = 10, scale = 2)
    var totalAmount: BigDecimal,

    var stripePaymentIntentId: String? = null,

    var trackingCode: String? = null,

    @Column(nullable = false)
    var createdAt: LocalDateTime = LocalDateTime.now(),

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,
) {
    @OneToMany(mappedBy = "order", cascade = [CascadeType.ALL], orphanRemoval = true)
    var items: MutableList<OrderItem> = mutableListOf()
}
