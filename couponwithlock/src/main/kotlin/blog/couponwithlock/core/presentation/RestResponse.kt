package blog.couponwithlock.core.presentation

import java.time.LocalDateTime

data class RestResponse<T>(
    val data: T,
    override val success: Boolean,
    override val timestamp: LocalDateTime = LocalDateTime.now()
) : BaseResponse(success, timestamp)
