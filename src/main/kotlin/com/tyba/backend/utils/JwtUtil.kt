package com.tyba.backend.utils

import com.tyba.backend.constants.DefaultValues.DEFAULT_STRING_VALUE
import com.tyba.backend.constants.ErrorCode.GENERIC_UNAUTHORIZED_ERROR
import com.tyba.backend.exceptions.LoginException
import com.tyba.backend.services.RedisService
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.AuthorityUtils
import org.springframework.stereotype.Component
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes
import java.util.Date
import java.util.stream.Collectors

@Component
class JwtUtil(

    private val redisService: RedisService
) {

    companion object {

        const val EXPIRATION_IN_MILLISECONDS = 600000
        const val HEADER = "Authorization"
        const val PREFIX = "Bearer "
        const val SECRET = "mySecretKey"
    }

    fun getJWTToken(email: String): String {

        val secretKey = "mySecretKey"

        val grantedAuthorities: List<GrantedAuthority> = AuthorityUtils
            .commaSeparatedStringToAuthorityList("ROLE_USER")

        val token: String = Jwts
            .builder()
            .setId("tybaJWT")
            .setSubject(email)
            .claim(
                "authorities",
                grantedAuthorities.stream()
                    .map<Any>(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList())
            )
            .setIssuedAt(Date(System.currentTimeMillis()))
            .setExpiration(Date(System.currentTimeMillis() + EXPIRATION_IN_MILLISECONDS))
            .signWith(
                SignatureAlgorithm.HS512,
                secretKey.toByteArray()
            ).compact()
        return "$PREFIX$token"
    }

    fun validateToken(isLogout: Boolean = false): String {

        val request = (RequestContextHolder.getRequestAttributes() as ServletRequestAttributes).request

        val authHeader = request.getHeader(HEADER) ?: throw LoginException(GENERIC_UNAUTHORIZED_ERROR)

        val jwtToken = authHeader.replace(PREFIX, DEFAULT_STRING_VALUE)

        val claims =
            Jwts.parser().setSigningKey(SECRET.toByteArray()).parseClaimsJws(jwtToken).body

        val redisLogout = redisService.getTokenData(jwtToken)

        if(isLogout && redisLogout == null) {

            redisService.putTokenData(jwtToken)
        }

        return if (claims["authorities"] == null || redisLogout != null) {

            throw LoginException(GENERIC_UNAUTHORIZED_ERROR)
        }
        else {

            claims.subject
        }
    }
}