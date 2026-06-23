package nl.wendyarthouse.wa_backend.model

import jakarta.persistence.*
import java.math.BigDecimal

@Entity
@Table(name = "order_items")
class OrderItem(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    var order: Order,

    @Column(nullable = false)
    var productName: String,

    @Column(nullable = false, precision = 10, scale = 2)
    var priceAtPurchase: BigDecimal,

    @Column(nullable = false)
    var quantity: Int,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,
)
