package com.tyba.backend.models

import com.fasterxml.jackson.databind.PropertyNamingStrategy.SnakeCaseStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming
import java.io.Serializable
import java.time.LocalDateTime

@JsonNaming(SnakeCaseStrategy::class)
data class UserTransactionRestaurant(

    var id: String = "",

    var transactionId: String = "",

    var city: String? = "",

    var lat: Double? = null,

    var lng: Double? = null,

    var createdAt: LocalDateTime = LocalDateTime.now(),

    var name: String? = "",

    var address: String? = "",

    var phone: String? = "",

    var score: Double? = null
): Serializable
