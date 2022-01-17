package com.tyba.backend.services

import com.tyba.backend.constants.CacheKeys.LOGOUT_TOKENS
import org.springframework.cache.annotation.CachePut
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service

@Service
class RedisService {

    @Cacheable(
        value = [LOGOUT_TOKENS],
        key = "'$LOGOUT_TOKENS'+'_'+#token",
        unless = "#result == null || #result.isEmpty()",
        cacheManager = "cacheManager30min"
    )
    fun getTokenData(token: String?): String? {

        return null
    }

    @CachePut(
        value = [LOGOUT_TOKENS],
        key = "'$LOGOUT_TOKENS'+'_'+#token",
        cacheManager = "cacheManager30min"
    )
    fun putTokenData(token: String?): String? {

        return token
    }
}