package com.tyba.backend.models

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming
import java.io.Serializable

@JsonNaming(SnakeCaseStrategy::class)
data class HistoricalRestaurant(

    var id: String = "",

    var transactionId: String = "",

    var name: String? = "",

    var address: String? = "",

    var phone: String? = "",

    var score: Double? = null
): Serializable {

    companion object {
        const val TABLE = "historical_restaurant"
    }
}
