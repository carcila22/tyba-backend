package com.tyba.backend.repositories

import com.tyba.backend.models.HistoricalRestaurant
import com.tyba.backend.models.UserTransaction
import com.tyba.backend.models.UserTransactionRestaurant
import com.tyba.backend.queries.UserTransactionQuery
import com.tyba.backend.queries.UserTransactionQuery.insertHistoricalRestaurantByTransaction
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.jdbc.core.simple.SimpleJdbcInsert
import org.springframework.stereotype.Repository

@Repository
class UseTransactionRepository(

    @Qualifier("tybaBackendNamedParameterJdbcTemplate")
    private val namedParameterJdbcTemplate: NamedParameterJdbcTemplate
) {

    fun createUserTransaction(userTransaction: UserTransaction): Boolean {

        return SimpleJdbcInsert(namedParameterJdbcTemplate.jdbcTemplate)
            .withTableName(UserTransaction.TABLE)
            .execute(BeanPropertySqlParameterSource(userTransaction)) > 0
    }

    fun saveHistoricalRestaurantByTransactionInBatch(historicalRestaurants: List<HistoricalRestaurant>): IntArray {

        val parameters = historicalRestaurants.map {

            val parameters = MapSqlParameterSource()
            parameters.addValue("id", it.id)
            parameters.addValue("transaction_id", it.transactionId)
            parameters.addValue("name", it.name)
            parameters.addValue("address", it.address)
            parameters.addValue("phone", it.phone)
            parameters.addValue("score", it.score)

            parameters
        }

        return namedParameterJdbcTemplate.batchUpdate(
            insertHistoricalRestaurantByTransaction,
            parameters.toTypedArray()
        )
    }

    fun getUserTransactions(userEmail: String): List<UserTransactionRestaurant> {

        return namedParameterJdbcTemplate.query(
            UserTransactionQuery.selectTransactionByUserEmail,
            MapSqlParameterSource().addValue("userEmail", userEmail),
            BeanPropertyRowMapper(UserTransactionRestaurant::class.java)
        )
    }
}