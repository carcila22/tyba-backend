package com.tyba.backend.configurations

import org.springframework.context.MessageSource
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.support.ResourceBundleMessageSource

@Configuration
class AppConfig {

    @Bean
    fun messageSource(): MessageSource {
        val resourceMessage = ResourceBundleMessageSource()
        resourceMessage.setBasename("i18n/messages")
        resourceMessage.setDefaultEncoding("UTF-8")
        return resourceMessage
    }
}
