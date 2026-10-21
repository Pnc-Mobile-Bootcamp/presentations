package com.pnc.jetpackcomposedemos.features.orders.data

interface OrderDataSource {

    suspend fun getOrders(): List<OrderLineItemDto>

}


