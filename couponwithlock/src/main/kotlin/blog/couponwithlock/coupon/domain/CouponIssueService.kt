package blog.couponwithlock.coupon.domain

import blog.couponwithlock.coupon.exception.CouponErrorCode
import org.springframework.stereotype.Service

@Service
class CouponIssueService(
    private val couponRepository: CouponRepository,
) {
    fun issueCoupon(userId: Long): Coupon? {
        couponRepository.findByUserId(userId)?.let {
            return it
        }

        val couponWithIdNull = couponRepository.findFirstByUserIdIsNullOrderByIdAsc()
            ?: throw CouponErrorCode.ALL_COUPON_ISSUED.toException()

        couponWithIdNull.userId = userId

        return couponWithIdNull
    }
}
