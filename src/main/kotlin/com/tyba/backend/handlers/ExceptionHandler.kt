package com.tyba.backend.handlers

import com.tyba.backend.constants.ErrorCode.GENERIC_BAD_REQUEST_ERROR
import com.tyba.backend.constants.ErrorCode.GENERIC_DATA_BASE_ERROR
import com.tyba.backend.constants.ErrorCode.GENERIC_ERROR
import com.tyba.backend.constants.ErrorCode.GENERIC_METHOD_NOT_ALLOWED
import com.tyba.backend.constants.ErrorCode.GENERIC_UNAUTHORIZED_ERROR
import com.tyba.backend.exceptions.Error
import com.tyba.backend.exceptions.ErrorContainer
import com.tyba.backend.exceptions.LoginException
import com.tyba.backend.exceptions.UserException
import com.tyba.backend.utils.MessageTranslator
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.MalformedJwtException
import io.jsonwebtoken.UnsupportedJwtException
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.DataAccessException
import org.springframework.http.HttpStatus
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.HttpRequestMethodNotSupportedException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.ResponseStatus
import javax.servlet.http.HttpServletRequest

@ControllerAdvice
class ExceptionHandler {

    @Autowired
    private lateinit var messageTranslator: MessageTranslator

    private val log = LoggerFactory.getLogger(this.javaClass)

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(UserException::class)
    fun handleCustomError(request: HttpServletRequest, exception: UserException): ErrorContainer {

        val errorMessage = messageTranslator.getMessage(code = exception.code, args = exception.messageArguments)
        log.warn(errorMessage)

        return ErrorContainer(
            Error(
                request.requestURI,
                exception.code,
                HttpStatus.BAD_REQUEST.value().toString(),
                errorMessage
            )
        )
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(LoginException::class)
    fun handleCustomError(request: HttpServletRequest, exception: LoginException): ErrorContainer {

        val errorMessage = messageTranslator.getMessage(code = exception.code, args = exception.messageArguments)
        log.warn(errorMessage)

        return ErrorContainer(
            Error(
                request.requestURI,
                exception.code,
                HttpStatus.UNAUTHORIZED.value().toString(),
                errorMessage
            )
        )
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @ExceptionHandler(HttpRequestMethodNotSupportedException::class)
    fun handleMethodNotSupportedError(
        request: HttpServletRequest,
        exception: HttpRequestMethodNotSupportedException
    ): ErrorContainer {
        val errorMessage = messageTranslator.getMessage(
            code = GENERIC_METHOD_NOT_ALLOWED,
            args = arrayOf(exception.method)
        )
        log.warn(exception.message, exception)

        return ErrorContainer(
            Error(
                request.requestURI,
                exception.message.toString(),
                HttpStatus.METHOD_NOT_ALLOWED.value().toString(),
                errorMessage
            )
        )
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun handleBadRequestError(request: HttpServletRequest, exception: HttpMessageNotReadableException): ErrorContainer {
        val errorMessage = messageTranslator.getMessage(
            code = GENERIC_BAD_REQUEST_ERROR
        )
        log.warn(exception.message, exception)

        return ErrorContainer(
            Error(
                request.requestURI,
                exception.message.toString(),
                HttpStatus.BAD_REQUEST.value().toString(),
                errorMessage
            )
        )
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationRequestError(request: HttpServletRequest, exception: MethodArgumentNotValidException): ErrorContainer {
        val errorMessage = messageTranslator.getMessage(
            code = GENERIC_BAD_REQUEST_ERROR
        )
        log.warn(exception.message, exception)

        return ErrorContainer(
            Error(
                request.requestURI,
                exception.message,
                HttpStatus.BAD_REQUEST.value().toString(),
                errorMessage
            )
        )
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(DataAccessException::class)
    fun handleDataBaseError(request: HttpServletRequest, exception: DataAccessException): ErrorContainer {
        val errorMessage = messageTranslator.getMessage(
            code = GENERIC_DATA_BASE_ERROR
        )
        log.error(errorMessage, exception)

        return ErrorContainer(
            Error(
                request.requestURI,
                exception.message.toString(),
                HttpStatus.INTERNAL_SERVER_ERROR.value().toString(),
                errorMessage
            )
        )
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception::class)
    fun handleGenericError(request: HttpServletRequest, ex: Exception): ErrorContainer {

        val errorMessage = messageTranslator.getMessage(code = GENERIC_ERROR)
        log.error(ex.message, ex)

        return ErrorContainer(
            Error(
                request.requestURI, GENERIC_ERROR, HttpStatus.INTERNAL_SERVER_ERROR.value().toString(), errorMessage
            )
        )
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(ExpiredJwtException::class)
    fun handleUnauthorizedError(request: HttpServletRequest, exception: ExpiredJwtException): ErrorContainer {
        val errorMessage = messageTranslator.getMessage(
            code = GENERIC_UNAUTHORIZED_ERROR
        )
        log.warn(exception.message, exception)

        return ErrorContainer(
            Error(
                request.requestURI,
                exception.message.toString(),
                HttpStatus.FORBIDDEN.value().toString(),
                errorMessage
            )
        )
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(UnsupportedJwtException::class)
    fun handleUnauthorizedError(request: HttpServletRequest, exception: UnsupportedJwtException): ErrorContainer {
        val errorMessage = messageTranslator.getMessage(
            code = GENERIC_UNAUTHORIZED_ERROR
        )
        log.warn(exception.message, exception)

        return ErrorContainer(
            Error(
                request.requestURI,
                exception.message.toString(),
                HttpStatus.FORBIDDEN.value().toString(),
                errorMessage
            )
        )
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(MalformedJwtException::class)
    fun handleUnauthorizedError(request: HttpServletRequest, exception: MalformedJwtException): ErrorContainer {
        val errorMessage = messageTranslator.getMessage(
            code = GENERIC_UNAUTHORIZED_ERROR
        )
        log.warn(exception.message, exception)

        return ErrorContainer(
            Error(
                request.requestURI,
                exception.message.toString(),
                HttpStatus.FORBIDDEN.value().toString(),
                errorMessage
            )
        )
    }
}
