package blog.couponwithlock.coupon.exception

import blog.couponwithlock.core.exception.error.BaseErrorCode
import blog.couponwithlock.core.exception.error.DomainException
import org.springframework.http.HttpStatus

enum class CouponErrorCode(
    override val httpStatus: HttpStatus,
    override val message: String
) : BaseErrorCode<DomainException> {

    ALL_COUPON_ISSUED(HttpStatus.NOT_FOUND, "모든 쿠폰이 발급되었습니다.");

    override fun toException(): DomainException {
        return DomainException(httpStatus, this)
    }
}
