package blog.couponwithlock.coupon.infrastructure

import org.hibernate.annotations.IdGeneratorType

@IdGeneratorType(CouponIdGenerator::class)
@Target(AnnotationTarget.FIELD)
annotation class CouponId
