package com.pnc.jetpackcomposedemos.testutil.builders

import com.pnc.jetpackcomposedemos.features.orders.domain.LineItem
import com.pnc.jetpackcomposedemos.features.orders.domain.Order
import java.time.LocalDateTime

class OrderBuilder {
    var id: Int = 1
    var customerId: Int = 100
    var firstName: String = "Ada"
    var lastName: String = "Lovelace"
    var orderDate: LocalDateTime = LocalDateTime.of(2024, 1, 1, 0, 0)
    var shipDate: LocalDateTime? = null     // delay setting the default value
                                            // for any with computed defaults

    private val items = mutableListOf<LineItem>()

    fun item(
        productName: String = "Widget",
        qty: Int = 1,
        unitPrice: Double = 10.0
    ) {
        items += aLineItem(
            id = items.size + 1,
            productName = productName,
            orderQty = qty,
            unitPrice = unitPrice
        )
    }

    fun build() = Order(
        id = id,
        customerId = customerId,
        firstName = firstName,
        lastName = lastName,
        orderDate = orderDate,
        shipDate = shipDate ?: orderDate.plusDays(2),   // computed default
        items = items.toList()
    )

}

fun anOrder(block: OrderBuilder.() -> Unit = {}): Order =
    OrderBuilder().apply(block).build()

