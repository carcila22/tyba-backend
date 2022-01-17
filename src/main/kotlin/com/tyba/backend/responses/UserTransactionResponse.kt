package com.tyba.backend.responses

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.PropertyNamingStrategy.SnakeCaseStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming
import com.tyba.backend.constants.TimeFormat.FULL_WITH_SECONDS
import java.io.Serializable
import java.time.LocalDateTime

@JsonNaming(SnakeCaseStrategy::class)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
data class UserTransactionResponse(

    var transactionId: String = "",

    var city: String? = "",

    var lat: Double? = null,

    var lng: Double? = null,

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = FULL_WITH_SECONDS)
    var createdAt: LocalDateTime? = null,

    var restaurants: List<RestaurantByCityOrCoordinatesResponse>? = listOf()
) : Serializable
