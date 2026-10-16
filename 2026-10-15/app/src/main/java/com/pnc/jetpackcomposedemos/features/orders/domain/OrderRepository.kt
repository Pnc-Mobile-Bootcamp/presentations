package com.pnc.jetpackcomposedemos.features.orders.domain

interface OrderRepository {

    suspend fun getOrders(): List<Order>

}



