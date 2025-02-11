package blog.couponwithlock.core.exception.error

import org.springframework.http.HttpStatus

enum class GlobalErrorCode(
    override val httpStatus: HttpStatus,
    override val message: String
) : BaseErrorCode<DomainException> {

    FAILED_TO_ACQUIRE_LOCK(HttpStatus.INTERNAL_SERVER_ERROR, "분산락 획득에 실패했습니다.");

    override fun toException(): DomainException {
        return DomainException(httpStatus, this)
    }
}
