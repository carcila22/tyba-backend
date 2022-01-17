package com.tyba.backend.controllers

import com.tyba.backend.constants.ErrorCode.GENERIC_BAD_REQUEST_ERROR
import com.tyba.backend.constants.Routes.LOGIN
import com.tyba.backend.constants.Routes.LOGOUT
import com.tyba.backend.constants.Routes.RESTAURANTS
import com.tyba.backend.constants.Routes.TRANSACTIONS
import com.tyba.backend.constants.Routes.USER
import com.tyba.backend.exceptions.UserException
import com.tyba.backend.requests.CreateUserRequest
import com.tyba.backend.responses.DefaultResponse
import com.tyba.backend.responses.LoginResponse
import com.tyba.backend.responses.RestaurantByCityOrCoordinatesResponse
import com.tyba.backend.responses.UserTransactionResponse
import com.tyba.backend.services.UserService
import com.tyba.backend.utils.JwtUtil
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
@RequestMapping(USER)
class UserController(

    private val userService: UserService,

    private val jwtUtil: JwtUtil
) {

    @PostMapping
    fun createUser(@Valid @RequestBody createRequest: CreateUserRequest) = userService.createUser(createRequest)

    @GetMapping(LOGIN)
    fun login(@RequestParam email: String, @RequestParam password: String): LoginResponse {

        return userService.login(email, password)
    }

    @GetMapping(RESTAURANTS)
    fun getRestaurantsByCityOrCoordinates(
        @RequestParam city: String?,
        lat: Double?,
        lng: Double?
    ): List<RestaurantByCityOrCoordinatesResponse> {

        val email = jwtUtil.validateToken()

        if (city == null && (lat == null || lng == null)) {

            throw UserException(GENERIC_BAD_REQUEST_ERROR)
        }

        return userService.getRestaurantsByCityOrCoordinates(city = city, lat = lat, lng = lng, userEmail = email)
    }

    @GetMapping(TRANSACTIONS)
    fun getUserTransactions(): List<UserTransactionResponse> {

        val email = jwtUtil.validateToken()

        return userService.getUserTransactions(email)
    }

    @GetMapping(LOGOUT)
    fun logout(): DefaultResponse {

        jwtUtil.validateToken(isLogout = true)

        return DefaultResponse(true)
    }
}