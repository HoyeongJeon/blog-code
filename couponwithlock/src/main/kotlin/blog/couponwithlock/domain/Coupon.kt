package blog.couponwithlock.domain

import blog.couponwithlock.infrastructure.CouponId
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id

@Entity
class Coupon(userId: Long) {
    @Id
    @CouponId
    val id: String? = null

    @Column(unique = true)
    final var userId: Long? = null

    init {
        this.userId = userId
    }
}
