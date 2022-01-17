package com.tyba.backend.requests

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.databind.PropertyNamingStrategy.SnakeCaseStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming
import java.io.Serializable
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank

@JsonNaming(SnakeCaseStrategy::class)
@JsonIgnoreProperties(ignoreUnknown = true)
data class CreateUserRequest(

    @field:NotBlank
    val name: String,

    @field:Email
    val email: String,

    @field:NotBlank
    val password: String
): Serializable
