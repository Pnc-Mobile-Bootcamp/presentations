package com.pnc.jetpackcomposedemos.testutil.builders

import com.pnc.jetpackcomposedemos.features.orders.data.MyOrderResponseDto
import com.pnc.jetpackcomposedemos.features.orders.data.MyOrderDto

fun aMyOrdersResponse(
    customerId: Int = 11000,
    vararg orders: MyOrderDto
) = MyOrderResponseDto(
    customerId = customerId,
    page = 1,
    pageSize = 100,
    totalOrders = orders.size,
    orders = orders.toList()
)



