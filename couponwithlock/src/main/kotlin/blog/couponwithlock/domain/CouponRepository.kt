package blog.couponwithlock.domain

import org.springframework.data.jpa.repository.JpaRepository

interface CouponRepository : JpaRepository<Coupon, String>{
    fun findByUserId(userId: Long): Coupon?
}
