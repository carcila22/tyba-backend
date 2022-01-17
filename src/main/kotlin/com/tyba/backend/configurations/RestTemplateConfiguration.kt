package com.tyba.backend.configurations

import org.apache.http.HeaderElementIterator
import org.apache.http.client.config.RequestConfig
import org.apache.http.conn.ConnectionKeepAliveStrategy
import org.apache.http.impl.client.CloseableHttpClient
import org.apache.http.impl.client.HttpClients
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager
import org.apache.http.message.BasicHeaderElementIterator
import org.apache.http.protocol.HTTP
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.web.client.RestTemplate
import java.util.concurrent.TimeUnit

@Configuration
@EnableScheduling
class RestTemplateConfiguration {

    @Value("\${http_request_timeout}")
    private val httpRequestTimeout: Int = 3000

    @Value("\${http_read_timeout}")
    private val httpReadTimeout: Int = 3000

    @Value(value = "\${http_pool_keep_alive_time}")
    private val httpPoolKeepAliveTime: Long = 20000

    @Value(value = "\${http_pool_max_connections}")
    private val httpPoolMaxConnections: Int = 1000000

    @Value(value = "\${http_pool_max_route_connections}")
    private val httpPoolMaxRouteConnections: Int = 1200

    @Value(value = "\${http_pool_idle_connection_time}")
    private val httpPoolIdleConnectionTime: Long = 20000

    private fun getRestTemplate(connectionTimeOut: Int, readTimeout: Int, connectionRequestTimeout: Int): RestTemplate {

        return RestTemplate(clientHttpRequestFactory(connectionTimeOut, readTimeout, connectionRequestTimeout))
    }

    fun clientHttpRequestFactory(
        connectionTimeOut: Int,
        readTimeout: Int,
        connectionRequestTimeout: Int
    ): HttpComponentsClientHttpRequestFactory {

        val clientHttpRequestFactory = HttpComponentsClientHttpRequestFactory()
        clientHttpRequestFactory.httpClient = getHttpClient(connectionTimeOut, readTimeout, connectionRequestTimeout)
        clientHttpRequestFactory.setConnectTimeout(connectionTimeOut)
        clientHttpRequestFactory.setReadTimeout(readTimeout)
        clientHttpRequestFactory.setConnectionRequestTimeout(connectionRequestTimeout)

        return clientHttpRequestFactory
    }

    fun getHttpClient(connectionTimeOut: Int, readTimeout: Int, connectionRequestTimeout: Int): CloseableHttpClient {

        val requestConfig = RequestConfig.custom()
            .setConnectionRequestTimeout(connectionTimeOut)
            .setConnectTimeout(readTimeout)
            .setSocketTimeout(connectionRequestTimeout).build()

        return HttpClients.custom()
            .setDefaultRequestConfig(requestConfig)
            .setConnectionManager(poolingConnectionManager())
            .setKeepAliveStrategy(connectionKeepAliveStrategy())
            .build()
    }

    @Bean
    fun poolingConnectionManager(): PoolingHttpClientConnectionManager {

        val poolingConnectionManager = PoolingHttpClientConnectionManager()
        poolingConnectionManager.maxTotal = httpPoolMaxConnections
        poolingConnectionManager.defaultMaxPerRoute = httpPoolMaxRouteConnections

        return poolingConnectionManager
    }

    @Bean
    fun connectionKeepAliveStrategy(): ConnectionKeepAliveStrategy {

        return ConnectionKeepAliveStrategy { response, _ ->
            val it: HeaderElementIterator = BasicHeaderElementIterator(response.headerIterator(HTTP.CONN_KEEP_ALIVE))
            while (it.hasNext()) {
                val header = it.nextElement()
                if (header.value != null && header.name.equals("timeout", ignoreCase = true)) {
                    return@ConnectionKeepAliveStrategy header.value.toLong() * 1000
                }
            }
            httpPoolKeepAliveTime
        }
    }

    @Bean("restTemplate")
    fun restTemplate(): RestTemplate {

        return getRestTemplate(httpRequestTimeout, httpReadTimeout, httpReadTimeout)
    }

    @Scheduled(fixedDelayString = "\${http_pool_monitor_execution_time}")
    fun idleConnectionMonitor() {

        val connectionManager = poolingConnectionManager()

        connectionManager.closeExpiredConnections()
        connectionManager.closeIdleConnections(httpPoolIdleConnectionTime, TimeUnit.MILLISECONDS)
    }
}
