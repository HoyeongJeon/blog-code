package blog.couponwithlock.core.exception.error

import org.springframework.http.HttpStatus

class DomainException(val httpStatus: HttpStatus, errorCode: BaseErrorCode<*>) : RuntimeException(errorCode.message) {
    val code: String = errorCode.name
}
