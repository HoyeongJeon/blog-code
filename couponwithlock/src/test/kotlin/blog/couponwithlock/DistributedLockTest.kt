package blog.couponwithlock

import blog.couponwithlock.coupon.domain.CouponCountRepository
import blog.couponwithlock.coupon.domain.CouponIssueService
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicInteger
import kotlin.random.Random

@SpringBootTest
class DistributedLockTest(
    @Autowired
    private val couponIssueService: CouponIssueService,

    @Autowired
    private val couponCountRepository: CouponCountRepository
) {

    @BeforeEach
    fun setup() {
        couponCountRepository.reset()
    }

    @AfterEach
    fun tearDown() {
        couponCountRepository.reset()
    }

    @Test
    fun `동시 1000명 요청 시 중복 쿠폰 발생 없음`() {
        // given
        val executor = Executors.newFixedThreadPool(1000)
        val latch = CountDownLatch(1000)
        val successCount = AtomicInteger(0)
        val maxCouponCount = 100

        // when
       val results =  (1..1000).map {
           executor.submit {
               latch.await()
               try {
                   couponIssueService.issueCoupon(Random.nextLong())
                   successCount.incrementAndGet()
               } catch (e: Exception) {
                   println("${Thread.currentThread().name} - ${e.message}")
               }
           }.also { latch.countDown() }
       }

        // then
        results.forEach({it.get()})
        Thread.sleep(1000)
        assertThat(successCount.get()).isLessThanOrEqualTo(maxCouponCount)
        assertThat(couponCountRepository.count()).isEqualTo(maxCouponCount)

    }
}
