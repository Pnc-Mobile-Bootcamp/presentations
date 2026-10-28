package com.pnc.jetpackcomposedemos.features.orders.data

import com.pnc.jetpackcomposedemos.features.orders.domain.LineItem
import com.pnc.jetpackcomposedemos.features.orders.domain.Order
import java.time.LocalDateTime

object MyOrderMapper {

    fun mapToDomain(response: MyOrderResponseDto, customerName: String): List<Order> {

        val firstName = customerName.substringBefore(' ')
        val lastName = customerName.substringAfter(' ', missingDelimiterValue = "")

        return response.orders.map { myOrderDto ->
            val orderDate = LocalDateTime.parse(myOrderDto.orderDate)
            val shipDate = myOrderDto.shipDate?.let { LocalDateTime.parse(it) } ?: orderDate

            Order(
                id = myOrderDto.orderNumber,
                customerId = response.customerId,
                firstName = firstName,
                lastName = lastName,
                orderDate = orderDate,
                shipDate = shipDate,
                items = myOrderDto.lines.map { line ->
                    LineItem(
                        id = line.lineId,
                        productName = line.productName ?: "(unknown product)",
                        orderQty = line.quantity,
                        unitPrice = line.unitPrice,
                        lineTotal = line.lineTotal
                    )
                }
            )
        }

    }

}



