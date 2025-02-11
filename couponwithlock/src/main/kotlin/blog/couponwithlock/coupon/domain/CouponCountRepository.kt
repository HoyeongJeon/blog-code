package blog.couponwithlock.coupon.domain

import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Repository

@Repository
class CouponCountRepository(
    private val redisTemplate: RedisTemplate<String, String>
) {
    fun increment(): Long? {
        return redisTemplate
            .opsForValue()
            .increment("couponCount")
    }

    fun count(): Int?{
        return redisTemplate.opsForValue().get("couponCount")?.toInt()
    }

    fun reset() {
        redisTemplate.delete("couponCount")
    }
}
