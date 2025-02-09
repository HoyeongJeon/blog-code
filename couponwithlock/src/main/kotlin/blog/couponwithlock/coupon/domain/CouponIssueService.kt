package blog.couponwithlock.coupon.domain

import blog.couponwithlock.coupon.exception.CouponErrorCode
import org.springframework.stereotype.Service

@Service
class CouponIssueService(
    private val couponRepository: CouponRepository,
    private val couponCountRepository: CouponCountRepository
    ) {
    fun issueCoupon(userId: Long): Coupon {
        couponRepository.findByUserId(userId)?.let {
            return it
        }
        if (couponCountRepository.increment()!! > 100) {
            throw CouponErrorCode.ALL_COUPON_ISSUED.toException()
        }
        return couponRepository.save(Coupon.create(userId))
    }
}
