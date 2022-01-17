package com.tyba.backend.repositories

import com.tyba.backend.models.User
import com.tyba.backend.queries.UserQuery
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.jdbc.core.simple.SimpleJdbcInsert
import org.springframework.stereotype.Repository

@Repository
class UserRepository(

    @Qualifier("tybaBackendNamedParameterJdbcTemplate")
    private val namedParameterJdbcTemplate: NamedParameterJdbcTemplate
) {

    fun createUser(user: User): Boolean {

        return SimpleJdbcInsert(namedParameterJdbcTemplate.jdbcTemplate)
            .withTableName(User.TABLE)
            .execute(BeanPropertySqlParameterSource(user)) > 0
    }

    fun getByEmailAndPassword(email: String, password: String): User? {

        val parameters = MapSqlParameterSource()
        parameters.addValue("email", email)
        parameters.addValue("password", password)

        return namedParameterJdbcTemplate.query(
            UserQuery.selectByEmailAndPassword,
            parameters,
            BeanPropertyRowMapper(User::class.java)
        ).firstOrNull()
    }
}