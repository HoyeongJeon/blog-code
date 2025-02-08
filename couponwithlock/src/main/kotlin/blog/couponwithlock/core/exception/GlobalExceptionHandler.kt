package blog.couponwithlock.core.exception

import blog.couponwithlock.core.exception.error.DomainException
import blog.couponwithlock.core.presentation.ErrorResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {
    @ExceptionHandler(DomainException::class)
    fun handleDomainException(ex: DomainException): ResponseEntity<ErrorResponse> {
        val httpStatus = ex.httpStatus
        val errorResponse = ErrorResponse.createDomainErrorResponse(httpStatus.value(), ex)
        return ResponseEntity.status(httpStatus).body(errorResponse)
    }
}
