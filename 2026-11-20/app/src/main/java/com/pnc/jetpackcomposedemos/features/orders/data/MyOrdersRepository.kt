package com.pnc.jetpackcomposedemos.features.orders.data

import com.pnc.jetpackcomposedemos.features.auth.domain.SessionManager
import com.pnc.jetpackcomposedemos.features.orders.domain.Order
import com.pnc.jetpackcomposedemos.features.orders.domain.OrderRepository
import javax.inject.Inject

class MyOrdersRepository @Inject constructor(
    private val dataSource: MyOrdersDataSource,
    private val session: SessionManager
): OrderRepository {

    override suspend fun getOrders(): List<Order> {

        val user = session.currentUser
            ?: throw IllegalStateException("You must be signed in to see your orders.")

        val customerId = user.id.toIntOrNull()
            ?: throw IllegalStateException("The signed-in user has no valid customer id.")

        val response = dataSource.getOrders(customerId, 1, 100)
        return MyOrderMapper.mapToDomain(response, user.username)

    }

}


