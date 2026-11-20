package com.pnc.jetpackcomposedemos.features.orders.data

data class MyOrderLineDto(
    val lineId: Int,
    val productId: Int,
    val productName: String?,
    val quantity: Int,
    val unitPrice: Double,
    val unitPriceDiscount: Double,
    val lineTotal: Double
)

data class MyOrderDto(
    val orderNumber: Int,
    val orderReference: String?,
    val orderDate: String,
    val shipDate: String?,
    val status: Int,
    val statusName: String,
    val subTotal: Double,
    val taxAmt: Double,
    val freight: Double,
    val totalDue: Double,
    val lines: List<MyOrderLineDto>
)

data class MyOrderResponseDto(
    val customerId: Int,
    val page: Int,
    val pageSize: Int,
    val totalOrders: Int,
    val orders: List<MyOrderDto>
)



