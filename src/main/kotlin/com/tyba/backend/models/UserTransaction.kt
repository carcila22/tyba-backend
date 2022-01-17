package com.tyba.backend.models

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming
import java.io.Serializable
import java.time.LocalDateTime

@JsonNaming(SnakeCaseStrategy::class)
data class UserTransaction(

    var id: String = "",

    var userId: String = "",

    var city: String? = "",

    var lat: Double? = null,

    var lng: Double? = null,

    var createdAt: LocalDateTime = LocalDateTime.now()
): Serializable {

    companion object {
        const val TABLE = "user_historical_transaction"
    }
}
