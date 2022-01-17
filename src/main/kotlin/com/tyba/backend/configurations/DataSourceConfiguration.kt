package com.tyba.backend.configurations

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.flyway.FlywayDataSource
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.transaction.annotation.EnableTransactionManagement
import javax.sql.DataSource

@Configuration
@EnableTransactionManagement
@ConfigurationProperties(prefix = "spring.datasource")
class DataSourceConfiguration : HikariConfig() {

    @Value("\${database.max-pool-size}")
    private val dbMaxPoolSize: Int = 10

    @Value("\${database.connection-timeout}")
    private val dbConnectionTimeout: Long = 20000

    @Value("\${database.minimum-idle}")
    private val dbMinimumIdle: Int = 1

    @Value("\${database.idle-timeout}")
    private val dbIdleTimeout: Long = 300000

    @Value("\${database.max-lifetime}")
    private val dbMaxLifetime: Long = 1200000

    @Value("\${database.leak-detection-threshold}")
    private val dbLeakDetectionThreshold: Long = 10

    @Bean
    @FlywayDataSource
    fun tybaBackendDataSource(): DataSource {

        val hikariDataSource = HikariDataSource(this)
        hikariDataSource.maximumPoolSize = dbMaxPoolSize
        hikariDataSource.connectionTimeout = dbConnectionTimeout
        hikariDataSource.minimumIdle = dbMinimumIdle
        hikariDataSource.maxLifetime = dbMaxLifetime
        hikariDataSource.idleTimeout = dbIdleTimeout
        hikariDataSource.leakDetectionThreshold = dbLeakDetectionThreshold

        return hikariDataSource
    }

    @Bean
    fun tybaBackendNamedParameterJdbcTemplate(
        @Qualifier("tybaBackendDataSource") cnsEarningDataSource: DataSource
    ): NamedParameterJdbcTemplate {

        return NamedParameterJdbcTemplate(cnsEarningDataSource)
    }
}
