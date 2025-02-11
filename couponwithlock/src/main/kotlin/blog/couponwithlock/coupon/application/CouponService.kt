package blog.couponwithlock.coupon.application

import blog.couponwithlock.coupon.domain.CouponIssueService
import blog.couponwithlock.coupon.presentation.dto.request.CouponIssueRequest
import blog.couponwithlock.coupon.presentation.dto.response.CouponIssueResponse
import org.springframework.stereotype.Service

@Service
class CouponService(
    private val couponIssueService: CouponIssueService
) {
    fun issueCoupon(couponIssueRequest: CouponIssueRequest): CouponIssueResponse {
        return CouponIssueResponse(couponIssueService.issueCoupon(couponIssueRequest.userId)?.id!!)
    }
}
