package com.tyba.backend.mappers

import com.tyba.backend.constants.DefaultValues.DEFAULT_STRING_VALUE
import com.tyba.backend.models.HistoricalRestaurant
import com.tyba.backend.models.User
import com.tyba.backend.models.UserTransaction
import com.tyba.backend.models.UserTransactionRestaurant
import com.tyba.backend.requests.CreateUserRequest
import com.tyba.backend.responses.RestaurantByCityOrCoordinatesResponse
import com.tyba.backend.responses.RestaurantResponse
import com.tyba.backend.responses.UserTransactionResponse
import org.springframework.stereotype.Component

@Component
class UserMapper {

    fun toUser(createUserRequest: CreateUserRequest, encryptPassword: String): User {

        return User(
            email = createUserRequest.email,
            name = createUserRequest.name,
            password = encryptPassword
        )
    }

    fun toRestaurantByCityOrCoordinatesResponse(apiResponse: List<RestaurantResponse>)
    : List<RestaurantByCityOrCoordinatesResponse> {

        return apiResponse.map { restaurantResponse ->

            RestaurantByCityOrCoordinatesResponse(
                id = restaurantResponse.id,
                name = restaurantResponse.poi?.name,
                phone = restaurantResponse.poi?.phone,
                address = restaurantResponse.address?.freeformAddress,
                score = restaurantResponse.score
            )
        }
    }

    fun toUserTransaction(
        transactionId: String,
        userEmail: String,
        city: String?,
        lat: Double?,
        lng: Double?
    ): UserTransaction {

        return UserTransaction(
            id = transactionId,
            userId = userEmail,
            city = city,
            lat = lat,
            lng = lng
        )
    }

    fun toHistoricalRestaurants(transactionId: String, restaurants: List<RestaurantResponse>): List<HistoricalRestaurant> {

        return restaurants.map {

            HistoricalRestaurant(
                id = it.id ?: DEFAULT_STRING_VALUE,
                transactionId = transactionId,
                name = it.poi?.name,
                phone = it.poi?.phone,
                address = it.address?.freeformAddress,
                score = it.score
            )
        }
    }

    fun toRestaurantByCityOrCoordinatesResponse(
        userTransactionRestaurants: List<UserTransactionRestaurant>,
        transactionId: String
    )
        : List<RestaurantByCityOrCoordinatesResponse> {

        return userTransactionRestaurants.filter { it.transactionId == transactionId }
            .map { userTransactionRestaurant ->

                RestaurantByCityOrCoordinatesResponse(
                    id = userTransactionRestaurant.id,
                    name = userTransactionRestaurant.name,
                    phone = userTransactionRestaurant.phone,
                    address = userTransactionRestaurant.address,
                    score = userTransactionRestaurant.score
                )
            }
    }

    fun toUserTransactionResponse(userTransactionRestaurants: List<UserTransactionRestaurant>): List<UserTransactionResponse> {

        return userTransactionRestaurants.distinctBy { it.transactionId }.map {

            UserTransactionResponse(
                transactionId = it.transactionId,
                city = it.city,
                lat = it.lat,
                lng = it.lng,
                createdAt = it.createdAt,
                restaurants = toRestaurantByCityOrCoordinatesResponse(
                    userTransactionRestaurants = userTransactionRestaurants,
                    transactionId = it.transactionId
                )
            )
        }
    }
}