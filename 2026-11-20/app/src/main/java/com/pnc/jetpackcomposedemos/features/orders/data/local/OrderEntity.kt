package com.pnc.jetpackcomposedemos.features.orders.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "orders")
data class OrderEntity(
    @PrimaryKey
    val id: Int,
    val customerId: Int,
    val firstName: String,
    val lastName: String,
    val orderDate: LocalDateTime,
    val shipDate: LocalDateTime,
)


