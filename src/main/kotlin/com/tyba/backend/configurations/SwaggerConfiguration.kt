package com.tyba.backend.configurations

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.web.servlet.config.annotation.EnableWebMvc
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import springfox.documentation.builders.ApiInfoBuilder
import springfox.documentation.builders.PathSelectors
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.service.ApiInfo
import springfox.documentation.service.ApiKey
import springfox.documentation.service.AuthorizationScope
import springfox.documentation.service.SecurityReference
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spi.service.contexts.SecurityContext
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.swagger2.annotations.EnableSwagger2

@Configuration
@EnableSwagger2
@EnableWebMvc
@Profile("develop")
class SwaggerConfiguration : WebMvcConfigurer {

    companion object Configuration {

        const val API_TITLE = "TYBA-BACKEND"
        const val API_DESCRIPTION = "TYBA-BACKEND"
        const val API_VERSION = "1.0"
        const val BASE_PACKAGE = "com.tyba.backend.controllers"
    }

    @Bean
    fun apiDocket(): Docket {

        return Docket(DocumentationType.SWAGGER_2)
            .select()
            .apis(RequestHandlerSelectors.basePackage(BASE_PACKAGE))
            .paths(PathSelectors.any())
            .build()
            .securitySchemes(listOf(getApiKey()))
            .securityContexts(listOf(securityContext()))
            .apiInfo(getApiInfo())
    }

    private fun securityContext(): SecurityContext {

        return SecurityContext.builder()
            .securityReferences(listOf(defaultAuth()))
            .forPaths(PathSelectors.any())
            .build()
    }

    private fun defaultAuth(): SecurityReference {

        val authScope = listOf(AuthorizationScope("global", "accessEverything"))

        return SecurityReference.builder()
            .reference("Token")
            .scopes(authScope.toTypedArray())
            .build()
    }

    private fun getApiKey() = ApiKey("Token", "Authorization", "header")

    private fun getApiInfo(): ApiInfo {

        return ApiInfoBuilder()
            .title(API_TITLE)
            .description(API_DESCRIPTION)
            .version(API_VERSION)
            .build()
    }

    @Override
    override fun addResourceHandlers(registry: ResourceHandlerRegistry) {
        registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/")
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/")
    }
}
