package com.pnc.jetpackcomposedemos.features.orders.data

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RemoteMyOrdersDataSource : MyOrdersDataSource {

    @GET("my/customers/{customerId}/orders")
    override suspend fun getOrders(
        @Path("customerId") customerId: Int,
        @Query("page") page: Int,
        @Query("pageSize") pageSize: Int
    ): MyOrderResponseDto

}


