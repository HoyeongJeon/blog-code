package blog.couponwithlock.coupon.domain

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface CouponRepository : JpaRepository<Coupon, Int>{
    fun findByUserId(userId: Long): Coupon?

    @Query("SELECT c FROM Coupon c WHERE c.userId IS NULL ORDER BY c.id ASC LIMIT 1")
    fun findFirstByUserIdIsNullOrderByIdAsc(): Coupon?
}
