package com.tyba.backend.services

import com.tyba.backend.constants.ErrorCode.GENERIC_LOGIN_ERROR
import com.tyba.backend.exceptions.LoginException
import com.tyba.backend.mappers.UserMapper
import com.tyba.backend.repositories.UseTransactionRepository
import com.tyba.backend.repositories.UserRepository
import com.tyba.backend.requesters.PoiSearchRequester
import com.tyba.backend.requests.CreateUserRequest
import com.tyba.backend.responses.DefaultResponse
import com.tyba.backend.responses.LoginResponse
import com.tyba.backend.responses.RestaurantByCityOrCoordinatesResponse
import com.tyba.backend.responses.RestaurantResponse
import com.tyba.backend.responses.UserTransactionResponse
import com.tyba.backend.utils.CypherUtil
import com.tyba.backend.utils.JwtUtil
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class UserService(

    private val userRepository: UserRepository,

    private val userTransactionRepository: UseTransactionRepository,

    private val poiSearchRequester: PoiSearchRequester,

    private val userMapper: UserMapper,

    private val jwtUtil: JwtUtil
) {

    fun createUser(createRequest: CreateUserRequest): DefaultResponse {

        val encryptPassword = CypherUtil.encrypt(createRequest.password)

        val user = userMapper.toUser(createRequest, encryptPassword)

        val response = userRepository.createUser(user)

        return DefaultResponse(response)
    }

    fun login(email: String, password: String): LoginResponse {

        val encryptPassword = CypherUtil.encrypt(password)

        userRepository.getByEmailAndPassword(email, encryptPassword) ?: throw LoginException(GENERIC_LOGIN_ERROR)

        val token = jwtUtil.getJWTToken(email)

        return LoginResponse(token)
    }

    fun getRestaurantsByCityOrCoordinates(
        city: String?,
        lat: Double?,
        lng: Double?,
        userEmail: String
    ): List<RestaurantByCityOrCoordinatesResponse> {

        val restaurantsResponse =
            poiSearchRequester.searchRestaurantsByCityNameOrCoordinates(city = city, lat = lat, lng = lng)

        createHistoricalInformation(
            userEmail = userEmail,
            city = city,
            lat = lat,
            lng = lng,
            restaurants = restaurantsResponse
        )

        return userMapper.toRestaurantByCityOrCoordinatesResponse(restaurantsResponse)
    }

    private fun createHistoricalInformation(
        userEmail: String,
        city: String?,
        lat: Double?,
        lng: Double?,
        restaurants: List<RestaurantResponse>
    ) {

        val transactionId = UUID.randomUUID().toString()

        val userTransaction = userMapper.toUserTransaction(
            transactionId = transactionId,
            userEmail = userEmail,
            city = city,
            lat = lat,
            lng = lng
        )

        userTransactionRepository.createUserTransaction(userTransaction)

        val historicalRestaurants =
            userMapper.toHistoricalRestaurants(transactionId = transactionId, restaurants = restaurants)

        userTransactionRepository.saveHistoricalRestaurantByTransactionInBatch(historicalRestaurants)
    }

    fun getUserTransactions(userEmail: String): List<UserTransactionResponse> {

        val transactions = userTransactionRepository.getUserTransactions(userEmail)

        return userMapper.toUserTransactionResponse(transactions)
    }
}