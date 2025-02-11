package blog.couponwithlock.coupon.domain

import blog.couponwithlock.core.exception.error.GlobalErrorCode
import blog.couponwithlock.coupon.exception.CouponErrorCode
import org.redisson.api.RedissonClient
import org.springframework.stereotype.Service
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.support.TransactionTemplate
import java.util.concurrent.TimeUnit

@Service
class CouponIssueService(
    private val couponRepository: CouponRepository,
    private val couponCountRepository: CouponCountRepository,
    private val redissonClient: RedissonClient,
    private val transactionManager: PlatformTransactionManager
) {
    fun issueCoupon(userId: Long): Coupon? {

        val lock = redissonClient.getLock("COUPON_ISSUE_DISTRIBUTED_LOCK")

        if (!lock.tryLock(2, 15, TimeUnit.SECONDS)) {
            throw GlobalErrorCode.FAILED_TO_ACQUIRE_LOCK.toException()
        }

        try {
            return TransactionTemplate(transactionManager).execute {
                couponRepository.findByUserId(userId)?.let {
                    return@execute it
                }

                val couponCount = couponCountRepository.count() ?: 0

                if (couponCount >= 100) {
                    throw CouponErrorCode.ALL_COUPON_ISSUED.toException()
                }

                couponCountRepository.increment()

                return@execute couponRepository.save(Coupon.create(userId))
            }
        } finally {
            if (lock.isHeldByCurrentThread) {
                lock.unlock()
            }
        }
    }
}
