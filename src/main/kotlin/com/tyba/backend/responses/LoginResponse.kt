package com.tyba.backend.responses

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming
import java.io.Serializable

@JsonNaming(SnakeCaseStrategy::class)
data class LoginResponse(

    val authenticationToken: String? = null
): Serializable