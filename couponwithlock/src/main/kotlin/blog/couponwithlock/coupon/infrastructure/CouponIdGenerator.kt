package blog.couponwithlock.coupon.infrastructure

import org.hibernate.engine.spi.SharedSessionContractImplementor
import org.hibernate.id.IdentifierGenerator
import java.io.Serializable
import java.security.SecureRandom

class CouponIdGenerator : IdentifierGenerator {
    private val ALLOWED_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
    private val RANDOM = SecureRandom();

    override fun generate(
        session: SharedSessionContractImplementor,
        `object`: Any?
    ): Serializable {
        val sb = StringBuilder(10)

        repeat(10) {
            val selectedChar = ALLOWED_CHARS[RANDOM.nextInt(ALLOWED_CHARS.length)]
            sb.append(selectedChar)
        }

        val result = sb.toString()

        require(result.length == 10) { "생성된 문자열 길이 오류: ${result.length}" }

        return result
    }
}
