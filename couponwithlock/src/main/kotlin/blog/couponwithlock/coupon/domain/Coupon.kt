package blog.couponwithlock.coupon.domain

import jakarta.persistence.*

@Entity
class Coupon(
    @Id
    @GeneratedValue
    private val id : Long? = null,

    val code: String? = null,

    @Column(unique = true)
    var userId: Long? = null,

    @Version
    private val version: Long = 0L
)
