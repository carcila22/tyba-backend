package com.tyba.backend.requesters

import com.tyba.backend.constants.ErrorCode.GENERIC_BAD_REQUEST_ERROR
import com.tyba.backend.constants.ErrorCode.GENERIC_NOT_FOUND_ERROR
import com.tyba.backend.constants.ErrorCode.GENERIC_UNAUTHORIZED_ERROR
import com.tyba.backend.exceptions.UserException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component

@Component
abstract class BaseMsRequester {

    @Autowired
    protected lateinit var requester: Requester

    @Throws(UserException::class)
    fun <T> validateResponse(
        response: ResponseEntity<T>,
        throw500: Boolean = false,
        throwResponseError: Boolean = false
    ) {
        if (response.statusCode.is4xxClientError) {

            if (response.statusCode === HttpStatus.NOT_FOUND) {
                throw UserException(GENERIC_NOT_FOUND_ERROR)
            }

            if (throwResponseError && response.statusCode === HttpStatus.BAD_REQUEST) {
                throw UserException(GENERIC_BAD_REQUEST_ERROR)
            }

            if (throwResponseError && response.statusCode === HttpStatus.UNAUTHORIZED) {
                throw UserException(GENERIC_UNAUTHORIZED_ERROR)
            }
        }

        if (throw500 && response.statusCode.is5xxServerError) {
            throw Exception()
        }
    }
}
