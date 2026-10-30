package com.pnc.jetpackcomposedemos.features.orders.data

import junit.framework.TestCase.assertEquals
import org.junit.Test
import java.time.LocalDateTime


class MyOrderMapperTest {

    private fun response(vararg orders: MyOrderDto) = MyOrderResponseDto(
        customerId = 11000, page = 1, pageSize = 100, totalOrders = orders.size, orders = orders.toList()
    )

    private fun order(number: Int, shipDate: String?, vararg lines: MyOrderLineDto) = MyOrderDto(
        orderNumber = number, orderReference = "SO$number", orderDate = "2014-05-01T00:00:00",
        shipDate = shipDate, status = 5, statusName = "Shipped", subTotal = 100.0, taxAmt = 8.0,
        freight = 5.0, totalDue = 113.0, lines = lines.toList()
    )

    private fun line(id: Int, name: String?) = MyOrderLineDto(
        lineId = id, productId = 7, productName = name, quantity = 2, unitPrice = 50.0,
        unitPriceDiscount = 0.0, lineTotal = 100.0
    )

    @Test
    fun `maps an order with its lines`() {
        val orders = MyOrderMapper.mapToDomain(
            response(order(100, "2014-05-08T00:00:00", line(1, "Road Bike"))),
            customerName = "Cara Cole"
        )

        val mapped = orders.single()
        assertEquals(100, mapped.id)
        assertEquals(11000, mapped.customerId)
        assertEquals("Cara", mapped.firstName)
        assertEquals("Cole", mapped.lastName)
        assertEquals(LocalDateTime.of(2014, 5, 1, 0, 0), mapped.orderDate)
        assertEquals(LocalDateTime.of(2014, 5, 8, 0, 0), mapped.shipDate)
        assertEquals("Road Bike", mapped.items.single().productName)
        assertEquals(100.0, mapped.items.single().lineTotal, 0.0)
    }

    @Test
    fun `an order that has not shipped falls back to its order date`() {
        val mapped = MyOrderMapper.mapToDomain(
            response(order(100, shipDate = null, line(1, "Road Bike"))), "Cara Cole"
        ).single()

        assertEquals(mapped.orderDate, mapped.shipDate)
    }

    @Test
    fun `a missing product name gets a placeholder`() {
        val mapped = MyOrderMapper.mapToDomain(
            response(order(100, "2014-05-08T00:00:00", line(1, null))), "Cara Cole"
        ).single()

        assertEquals("(unknown product)", mapped.items.single().productName)
    }

    @Test
    fun `a one-word name has an empty last name`() {
        val mapped = MyOrderMapper.mapToDomain(
            response(order(100, "2014-05-08T00:00:00")), "Madonna"
        ).single()

        assertEquals("Madonna", mapped.firstName)
        assertEquals("", mapped.lastName)
    }

    @Test
    fun `no orders gives an empty list`() {
        assertEquals(emptyList<Any>(), MyOrderMapper.mapToDomain(response(), "Cara Cole"))
    }
}




