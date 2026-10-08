package com.pnc.jetpackcomposedemos.features.orders.data

import com.pnc.jetpackcomposedemos.features.orders.domain.Order
import com.pnc.jetpackcomposedemos.features.orders.domain.OrderRepository
import javax.inject.Inject

class DefaultOrderRepository @Inject constructor(
    private val dataSource: OrderDataSource
): OrderRepository {

    override suspend fun getOrders(): List<Order> {
        val data = dataSource.getOrders()
        return OrderMapper.mapToDomain(data)
    }


}


