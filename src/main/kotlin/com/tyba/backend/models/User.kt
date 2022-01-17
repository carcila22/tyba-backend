package com.tyba.backend.models

import com.fasterxml.jackson.databind.PropertyNamingStrategy.SnakeCaseStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming
import java.io.Serializable
import java.time.LocalDateTime

@JsonNaming(SnakeCaseStrategy::class)
data class User(

    var email: String = "",

    var name: String = "",

    var password: String = "",

    var createdAt: LocalDateTime = LocalDateTime.now()
): Serializable {

    companion object {
        const val TABLE = "user_data"
    }
}
