package blog.couponwithlock.coupon.presentation

import blog.couponwithlock.core.presentation.RestResponse
import blog.couponwithlock.coupon.application.CouponService
import blog.couponwithlock.coupon.presentation.dto.request.CouponIssueRequest
import blog.couponwithlock.coupon.presentation.dto.response.CouponIssueResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/coupon")
class CouponIssueController(
    private val couponService: CouponService
) {

    @PostMapping("/issue")
    fun issueCoupon(@RequestBody couponIssueRequest: CouponIssueRequest): ResponseEntity<RestResponse<CouponIssueResponse>> {
        return ResponseEntity.ok(RestResponse(couponService.issueCoupon(couponIssueRequest), true))
    }
}
