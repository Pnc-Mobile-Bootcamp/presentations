package com.pnc.jetpackcomposedemos.features.orders.data

import com.pnc.jetpackcomposedemos.features.orders.data.local.OrderDao
import com.pnc.jetpackcomposedemos.features.orders.data.local.OrderEntityMapper
import com.pnc.jetpackcomposedemos.features.orders.domain.Order
import com.pnc.jetpackcomposedemos.features.orders.domain.OrderRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalOnlyOrderRepository @Inject constructor(
    private val orderDao: OrderDao
): OrderRepository {

    override suspend fun getOrders(): List<Order> {
        return orderDao.getOrdersWithLineItems().map {
            OrderEntityMapper.mapToDomain(it)
        }
    }

}


