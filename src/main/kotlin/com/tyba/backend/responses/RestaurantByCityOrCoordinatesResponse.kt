package com.tyba.backend.responses

import java.io.Serializable

data class RestaurantByCityOrCoordinatesResponse(

    var id: String? = "",

    var name: String? = "",

    var phone: String? = "",

    var address: String? = "",

    var score: Double? = 0.0
): Serializable
