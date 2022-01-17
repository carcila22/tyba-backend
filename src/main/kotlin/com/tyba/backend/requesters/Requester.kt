package com.tyba.backend.requesters

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate

@Component
class Requester {

    companion object {

        const val X_APPLICATION_ID_HEADER = "X-Application-ID"
    }

    val logger: Logger = LoggerFactory.getLogger(this.javaClass)

    @Value("\${ms.header.x-application-id}")
    val xApplicationIdValue: String = ""

    @Qualifier("restTemplate")
    @Autowired
    lateinit var restTemplate: RestTemplate

    fun <T> execute(
        url: String,
        method: HttpMethod,
        requestEntity: Any? = null,
        responseEntity: Class<T>,
        headers: HashMap<String, String>? = null,
        injectedRestTemplate: RestTemplate = restTemplate
    ): ResponseEntity<T> {

        val request = HttpEntity(requestEntity, this.getHeaders(headers))

        val response: ResponseEntity<T> = try {

            injectedRestTemplate.exchange(url, method, request, responseEntity)
        } catch (exception: Exception) {

            logger.error(
                "Url {},  Error message {},  with request {}  with method {}",
                url,
                exception.message,
                jacksonObjectMapper().writeValueAsString(requestEntity),
                method.toString()
            )

            ResponseEntity(HttpStatus.NOT_FOUND)
        }

        executeHandlerStatusCode(url, method, requestEntity, response)
        return response
    }

    private fun getBasicHeaders(): HttpHeaders {

        val headers = HttpHeaders()

        headers.contentType = MediaType.APPLICATION_JSON
        headers[X_APPLICATION_ID_HEADER] = xApplicationIdValue

        return headers
    }


    fun getHeaders(headerHashMap: HashMap<String, String>?): HttpHeaders {

        val httpHeaders = getBasicHeaders()

        if (headerHashMap != null) {

            for (header in headerHashMap) {

                httpHeaders[header.key] = header.value
            }
        }

        return httpHeaders
    }


    private fun <T> executeHandlerStatusCode(
        url: String,
        method: HttpMethod,
        requestEntity: Any? = null,
        response: ResponseEntity<T>
    ) {

        if (response.statusCode.is4xxClientError) {
            logger.error(
                "Url {},  Error 4xx response {},  with request {}  with method {}",
                url,
                jacksonObjectMapper().writeValueAsString(response.body),
                jacksonObjectMapper().writeValueAsString(requestEntity),
                method.toString()
            )
        }

        if (response.statusCode.is5xxServerError) {
            logger.error(
                "Url {},  Error 5xx response {},  with request {}  with method {}",
                url,
                jacksonObjectMapper().writeValueAsString(response.body),
                jacksonObjectMapper().writeValueAsString(requestEntity),
                method.toString()
            )
        }
    }
}
