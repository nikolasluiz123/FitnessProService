package br.com.fitnesspro.config.application.cache

import br.com.fitnesspro.core.cache.cacheNames
import com.github.benmanes.caffeine.cache.Caffeine
import org.springframework.cache.CacheManager
import org.springframework.cache.annotation.EnableCaching
import org.springframework.cache.caffeine.CaffeineCacheManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@EnableCaching
class CacheConfig {

    @Bean
    fun cacheManager(): CacheManager {
        val caffeine = Caffeine.newBuilder()
            .maximumSize(1000)

        return CaffeineCacheManager(*cacheNames).apply {
            isAllowNullValues = false
            setCaffeine(caffeine)
        }
    }
}