package com.tyba.backend.queries

import com.tyba.backend.models.User

object UserQuery {

    const val selectByEmailAndPassword = """
        SELECT *
        FROM ${User.TABLE}
        WHERE email = :email AND password = :password
    """

}