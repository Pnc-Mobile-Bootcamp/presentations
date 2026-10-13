package com.pnc.jetpackcomposedemos.features.orders.domain

import java.time.LocalDateTime

data class Order(
    val id: Int,
    val customerId: Int,
    val firstName: String,
    val lastName: String,
    val orderDate: LocalDateTime,
    val shipDate: LocalDateTime,
    val items: List<LineItem>
)



