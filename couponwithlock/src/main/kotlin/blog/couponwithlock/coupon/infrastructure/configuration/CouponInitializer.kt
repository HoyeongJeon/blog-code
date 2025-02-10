package blog.couponwithlock.coupon.infrastructure.configuration

import blog.couponwithlock.coupon.domain.Coupon
import blog.couponwithlock.coupon.domain.CouponRepository
import jakarta.annotation.PostConstruct
import jakarta.transaction.Transactional
import org.springframework.context.annotation.Configuration

@Configuration
class CouponInitializer(
    private val couponRepository: CouponRepository
) {
    @PostConstruct
    @Transactional
    fun initCoupon() {
        for (i in 1..100) {
            couponRepository.save(Coupon(code = CouponIdGenerator.codeGenerate()))
        }
    }
}
