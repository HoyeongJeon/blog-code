package blog.couponwithlock.coupon.application

import blog.couponwithlock.coupon.domain.Coupon
import blog.couponwithlock.coupon.domain.CouponRepository
import blog.couponwithlock.coupon.presentation.dto.request.CouponIssueRequest
import blog.couponwithlock.coupon.presentation.dto.response.CouponIssueResponse
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class CouponService(
    private val couponRepository : CouponRepository
) {
    @Transactional
    fun issueCoupon(couponIssueRequest: CouponIssueRequest): CouponIssueResponse {
        val issuedCoupon = couponRepository.findByUserId(couponIssueRequest.userId);

        if (issuedCoupon != null) {
            return  CouponIssueResponse(couponId = issuedCoupon.id!!)
        }

        val coupon = couponRepository.save(Coupon(userId = couponIssueRequest.userId))
        return CouponIssueResponse(couponId = coupon.id!!)
    }
}
