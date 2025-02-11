package blog.couponwithlock.coupon.domain

import org.springframework.data.jpa.repository.JpaRepository

interface CouponRepository : JpaRepository<Coupon, Int>{
    fun findByUserId(userId: Long): Coupon?
}
