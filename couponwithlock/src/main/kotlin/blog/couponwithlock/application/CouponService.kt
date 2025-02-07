package blog.couponwithlock.application

import blog.couponwithlock.domain.Coupon
import blog.couponwithlock.domain.CouponRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class CouponService(
    private val couponRepository : CouponRepository
) {
    @Transactional
    fun issueCoupon(userId: Long): Coupon {
        val issuedCoupon = couponRepository.findByUserId(userId);

        if (issuedCoupon != null) {
            return issuedCoupon
        }

        val newCoupon = Coupon(userId = userId)
        return couponRepository.save(newCoupon)
    }
}
