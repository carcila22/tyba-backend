package com.tyba.backend.queries

import com.tyba.backend.models.HistoricalRestaurant
import com.tyba.backend.models.UserTransaction

object UserTransactionQuery {

    const val insertHistoricalRestaurantByTransaction = """
        INSERT INTO ${HistoricalRestaurant.TABLE}
            (id, transaction_id, name, address, phone, score) 
        VALUES (:id, :transaction_id, :name, :address, :phone, :score) 
        ON CONFLICT (id) DO NOTHING
    """

    const val selectTransactionByUserEmail = """
        SELECT
            hr.transaction_id, uht.city, uht.lat, uht.lng, uht.created_at, 
            hr.id, hr.name, hr.address, hr.phone, hr.score
        FROM
            ${UserTransaction.TABLE} uht
        INNER JOIN ${HistoricalRestaurant.TABLE} hr ON
            uht.id = hr.transaction_id
        WHERE
            uht.user_id = :userEmail
    """

}