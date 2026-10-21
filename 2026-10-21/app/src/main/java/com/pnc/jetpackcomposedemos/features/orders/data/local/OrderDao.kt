package com.pnc.jetpackcomposedemos.features.orders.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction

@Dao
abstract class OrderDao {
    // needs to be an abstract class instead of an interface
    // so we can have some code in insertOrderWithLineItems

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertOrder(order: OrderEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertLineItems(items: List<LineItemEntity>)

    @Transaction
    open suspend fun insertOrderWithLineItems(order: OrderWithLineItemsEntity) {
        // a @Transaction method with a body must be "open"
        // to being overridden in the auto-generated child class
        insertOrder(order.order)
        insertLineItems(order.lineItems.map {
            it.copy(orderId = order.order.id)
        })
    }

    @Transaction
    @Query("SELECT * FROM orders")
    abstract suspend fun getOrdersWithLineItems(): List<OrderWithLineItemsEntity>

}

