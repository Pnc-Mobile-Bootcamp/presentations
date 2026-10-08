package com.pnc.jetpackcomposedemos.features.orders.data

data class OrderLineItemDto(
    val id: Int,
    val customerId: Int,
    val firstName: String,
    val lastName: String,
    val orderDate: String,
    val shipDate: String,
    val productName: String,
    val orderNumber: Int,
    val orderQty: Int,
    val unitPrice: Double,
    val lineTotal: Double
)


