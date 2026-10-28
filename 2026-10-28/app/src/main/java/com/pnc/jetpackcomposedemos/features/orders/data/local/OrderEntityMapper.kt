package com.pnc.jetpackcomposedemos.features.orders.data.local

import com.pnc.jetpackcomposedemos.features.orders.domain.LineItem
import com.pnc.jetpackcomposedemos.features.orders.domain.Order

object OrderEntityMapper {

    fun mapToDomain(line: LineItemEntity): LineItem {
        return LineItem(
            id = line.id,
            productName = line.productName,
            orderQty = line.orderQty,
            unitPrice = line.unitPrice,
            lineTotal = line.lineTotal
        )
    }

    fun mapToDomain(order: OrderEntity): Order {
        return Order(
            id = order.id,
            customerId = order.customerId,
            firstName = order.firstName,
            lastName = order.lastName,
            orderDate = order.orderDate,
            shipDate = order.shipDate,
            items = emptyList()
        )
    }

    fun mapToDomain(order: OrderWithLineItemsEntity): Order {
        return Order(
            id = order.order.id,
            customerId = order.order.customerId,
            firstName = order.order.firstName,
            lastName = order.order.lastName,
            orderDate = order.order.orderDate,
            shipDate = order.order.shipDate,
            items = order.lineItems.map { item ->
                mapToDomain(item)
            }
        )
    }

    fun mapToEntity(order: Order): OrderWithLineItemsEntity {
        return OrderWithLineItemsEntity(
            order = OrderEntity(
                id = order.id,
                customerId = order.customerId,
                firstName = order.firstName,
                lastName = order.lastName,
                orderDate = order.orderDate,
                shipDate = order.shipDate
            ),
            lineItems = order.items.map { lineItem ->
                LineItemEntity(
                    id = lineItem.id,
                    orderId = order.id,
                    productName = lineItem.productName,
                    orderQty = lineItem.orderQty,
                    unitPrice = lineItem.unitPrice,
                    lineTotal = lineItem.lineTotal
                )
            }
        )
    }

}



