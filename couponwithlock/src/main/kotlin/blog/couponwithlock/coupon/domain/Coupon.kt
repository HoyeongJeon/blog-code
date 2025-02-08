package blog.couponwithlock.coupon.domain

import blog.couponwithlock.coupon.infrastructure.CouponId
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id

@Entity
class Coupon constructor(
    @Column(unique = true, nullable = false)
    val userId: Long
) {
    @Id
    @CouponId
    val id: String? = null

    companion object{
        fun create(userId: Long): Coupon {
            return  Coupon(userId)
        }
    }
}
