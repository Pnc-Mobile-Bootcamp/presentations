package com.pnc.jetpackcomposedemos.features.orders.data

import retrofit2.http.GET

interface RemoteOrderDataSource : OrderDataSource {

    @GET("Order/customer")
    override suspend fun getOrders(): List<OrderLineItemDto>

}


