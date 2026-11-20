package com.pnc.jetpackcomposedemos.testutil.builders

import com.pnc.jetpackcomposedemos.features.orders.data.OrderLineItemDto

fun aLineItemDto(
    id: Int = 1,
    orderNumber: Int = 1,
    customerId: Int = 100,
    firstName: String = "Ada",
    lastName: String = "Lovelace",
    orderDate: String = "2024-01-01T00:00:00",
    shipDate: String = "2024-01-03T00:00:00",
    productName: String = "Widget",
    orderQty: Int = 1,
    unitPrice: Double = 10.0,
    lineTotal: Double = orderQty * unitPrice
) = OrderLineItemDto(
    id, customerId, firstName, lastName, orderDate, shipDate,
    productName, orderNumber, orderQty, unitPrice, lineTotal
)

