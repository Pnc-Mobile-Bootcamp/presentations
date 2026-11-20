package com.pnc.jetpackcomposedemos.features.orders.data

interface MyOrdersDataSource {

    suspend fun getOrders(customerId: Int, page: Int, pageSize: Int): MyOrderResponseDto

}


