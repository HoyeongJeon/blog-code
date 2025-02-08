package blog.couponwithlock.core.presentation

import java.time.LocalDateTime

abstract class BaseResponse protected constructor(
    open val success: Boolean,
    open val timestamp: LocalDateTime
)
