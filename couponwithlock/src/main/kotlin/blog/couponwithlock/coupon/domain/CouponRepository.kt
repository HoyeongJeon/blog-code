package blog.couponwithlock.coupon.domain

import jakarta.persistence.LockModeType
import jakarta.persistence.QueryHint
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Lock
import org.springframework.data.jpa.repository.QueryHints

interface CouponRepository : JpaRepository<Coupon, Int>{
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @QueryHints(
        QueryHint(name = "jakarta.persistence.lock.timeout", value = "5000")
    )
    fun findByUserId(userId: Long): Coupon?

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    override fun count(): Long
}
