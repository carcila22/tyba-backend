package com.tyba.backend.utils

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.MessageSource
import org.springframework.stereotype.Component
import java.util.*

@Component
class MessageTranslator {

    @Value("\${locale}")
    private lateinit var localeEnvironment: String

    @Autowired
    private lateinit var messageSource: MessageSource

    fun getMessage(code: String, args: Array<Any>? = null, locale: Locale = Locale(localeEnvironment)): String {

        return messageSource.getMessage(code, args, locale)
    }
}
