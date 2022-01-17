package com.tyba.backend.responses

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.io.Serializable

data class SearchRestaurantsResponse(

    var results: List<RestaurantResponse>? = listOf()
) : Serializable

@JsonIgnoreProperties(ignoreUnknown = true)
data class RestaurantResponse(

    var id: String? = "",

    var poi: PoiResponse? = null,

    var address: AddressResponse? = null,

    var score: Double? = 0.0
) : Serializable

@JsonIgnoreProperties(ignoreUnknown = true)
data class PoiResponse(

    var name: String? = "",

    var phone: String? = ""
) : Serializable

@JsonIgnoreProperties(ignoreUnknown = true)
data class AddressResponse(

    var freeformAddress: String? = ""
) : Serializable
