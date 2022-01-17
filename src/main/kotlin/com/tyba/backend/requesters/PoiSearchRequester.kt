package com.tyba.backend.requesters

import com.tyba.backend.constants.Routes
import com.tyba.backend.responses.RestaurantResponse
import com.tyba.backend.responses.SearchRestaurantsResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Component
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.util.UriComponentsBuilder

@Component
class PoiSearchRequester : BaseMsRequester() {

    @Value("\${api_base_path}")
    private lateinit var apiBasePath: String

    @Value("\${api_key}")
    private lateinit var apiKey: String

    @Value("\${countrySet}")
    private lateinit var countrySet: String

    @Value("\${categorySet}")
    private lateinit var categorySet: String

    @Value("\${radius}")
    private lateinit var radius: String

    @Value("\${limit}")
    private lateinit var limit: String

    fun searchRestaurantsByCityNameOrCoordinates(city: String?, lat: Double?, lng: Double?): List<RestaurantResponse> {

        val params: MultiValueMap<String, String> = LinkedMultiValueMap()

        params.add("countrySet", countrySet)
        params.add("categorySet", categorySet)
        params.add("radius", radius)
        params.add("limit", limit)
        params.add("key", apiKey)

        if (lat != null && lng != null) {

            params.add("lat", lat.toString())
            params.add("lon", lng.toString())
        }

        val route = UriComponentsBuilder.fromHttpUrl(apiBasePath)
            .path(Routes.POI_SEARCH_BY_CITY_OR_COORDINATES)
            .queryParams(params)
            .buildAndExpand(mapOf("query" to city))
            .toUriString()

        val response = requester.execute(
            url = route,
            method = HttpMethod.GET,
            responseEntity = SearchRestaurantsResponse::class.java
        )

        validateResponse(response)

        return response.body?.results ?: listOf()
    }
}