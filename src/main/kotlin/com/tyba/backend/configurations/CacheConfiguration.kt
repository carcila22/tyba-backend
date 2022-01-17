package com.tyba.backend.configurations

import com.tyba.backend.constants.RedisTtl.MINUTE
import org.springframework.beans.factory.annotation.Value
import org.springframework.cache.CacheManager
import org.springframework.cache.annotation.CachingConfigurerSupport
import org.springframework.cache.annotation.EnableCaching
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.data.redis.cache.RedisCacheConfiguration
import org.springframework.data.redis.cache.RedisCacheManager
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.connection.RedisStandaloneConfiguration
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer
import org.springframework.data.redis.serializer.RedisSerializationContext
import redis.clients.jedis.JedisPoolConfig
import java.time.Duration

@EnableCaching
@Configuration
class CacheConfiguration : CachingConfigurerSupport() {

    @Value("\${redis_host}")
    private val redisHost: String = ""

    @Value("\${redis_port}")
    private val redisPort: Int = 0

    @Value("\${redis_max_connections}")
    private val maxConnections: Int = 10

    @Value("\${redis_min_idle}")
    private val minIdle: Int = 10

    @Value("\${redis_max_idle}")
    private val maxIdle: Int = 20

    @Value("\${redis_connection_time_out}")
    private val connectionTimeOut: Long = 5

    @Value("\${redis_default_expire_time_seg}")
    private val defaultExpireTime: Long = 300

    @Value("\${redis_cache_prefix}")
    private val redisCachePrefix: String = "tyba-backend:"

    fun jedisPoolConfig(): JedisPoolConfig {

        val poolConfiguration = JedisPoolConfig()

        poolConfiguration.maxTotal = maxConnections
        poolConfiguration.minIdle = minIdle
        poolConfiguration.maxIdle = maxIdle
        poolConfiguration.blockWhenExhausted = true

        return poolConfiguration
    }

    @Bean
    @Primary
    fun redisConnectionFactory(): RedisConnectionFactory {

        val jedisClientConfiguration = JedisClientConfiguration.builder()
            .connectTimeout(Duration.ofSeconds(connectionTimeOut))
            .usePooling()
            .poolConfig(this.jedisPoolConfig())
            .build()

        return JedisConnectionFactory(RedisStandaloneConfiguration(redisHost, redisPort), jedisClientConfiguration)
    }

    fun redisCacheConfiguration(): RedisCacheConfiguration {

        val serializationPair = RedisSerializationContext
            .SerializationPair
            .fromSerializer(JdkSerializationRedisSerializer())

        return RedisCacheConfiguration.defaultCacheConfig()
            .prefixCacheNameWith(redisCachePrefix)
            .entryTtl(Duration.ofSeconds(defaultExpireTime))
            .serializeValuesWith(serializationPair)
            .disableCachingNullValues()
    }

    fun getCacheManagerByTtl(ttl: Long): RedisCacheManager {

        val configuration = this.redisCacheConfiguration().entryTtl(Duration.ofSeconds(ttl))

        return RedisCacheManager.builder(this.redisConnectionFactory()).cacheDefaults(configuration).build()
    }

    @Bean
    @Primary
    override fun cacheManager(): CacheManager {

        val configuration = this.redisCacheConfiguration()

        return RedisCacheManager.builder(this.redisConnectionFactory()).cacheDefaults(configuration).build()
    }

    @Bean
    fun cacheManager30min() = getCacheManagerByTtl(30 * MINUTE)
}
