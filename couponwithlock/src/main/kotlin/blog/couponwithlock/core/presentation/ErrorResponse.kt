package blog.couponwithlock.core.presentation

import blog.couponwithlock.core.exception.error.DomainException
import java.time.LocalDateTime

data class ErrorResponse(
    val statusCode: Int,
    val code: String,
    val message: String,
) : BaseResponse(false, LocalDateTime.now()) {

    companion object {
        fun createDomainErrorResponse(statusCode: Int, exception: DomainException): ErrorResponse {
            return ErrorResponse(
                statusCode = statusCode,
                code = exception.code ?: "도메인 에러",
                message = exception.message ?: "도메인 에러가 발생했습니다."
            )
        }
    }
}
