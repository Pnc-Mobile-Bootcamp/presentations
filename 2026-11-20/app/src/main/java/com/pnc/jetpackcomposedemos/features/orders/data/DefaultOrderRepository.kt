package com.pnc.jetpackcomposedemos.features.orders.data

import com.pnc.jetpackcomposedemos.core.ApplicationScope
import com.pnc.jetpackcomposedemos.features.orders.data.local.OrderDao
import com.pnc.jetpackcomposedemos.features.orders.data.local.OrderEntityMapper
import com.pnc.jetpackcomposedemos.features.orders.domain.Order
import com.pnc.jetpackcomposedemos.features.orders.domain.OrderRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class DefaultOrderRepository @Inject constructor(
    private val dataSource: OrderDataSource,
    private val orderDao: OrderDao,
    @ApplicationScope private val scope: CoroutineScope // outlives any one screen
): OrderRepository {

    override suspend fun getOrders(): List<Order> {
        val data = dataSource.getOrders()
        val orders = OrderMapper.mapToDomain(data)

        // start a background task (on another thread)
        // to store these orders to Room db
        scope.launch(Dispatchers.IO) {
            try {
                orders.map {
                    OrderEntityMapper.mapToEntity(it)
                }.forEach {
                    orderDao.insertOrderWithLineItems(it)
                }
            } catch (e: Exception) {
                // log it; a failed cache write should NOT break the app
            }
        }

        return orders
    }


}


