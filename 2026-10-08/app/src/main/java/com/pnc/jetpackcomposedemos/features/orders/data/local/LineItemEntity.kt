package com.pnc.jetpackcomposedemos.features.orders.data.local

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "order_line_items",
    foreignKeys = [
        ForeignKey(
            entity = OrderEntity::class,
            parentColumns = ["id"],
            childColumns = ["orderId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index("orderId")
    ]
)
data class LineItemEntity(
    @PrimaryKey
    val id: Int,
    val orderId: Int,
    val productName: String,
    val orderQty: Int,
    val unitPrice: Double,
    val lineTotal: Double
)


