package dkprint.redisdbrollback

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class RedisDbRollbackApplication

fun main(args: Array<String>) {
    runApplication<RedisDbRollbackApplication>(*args)
}
