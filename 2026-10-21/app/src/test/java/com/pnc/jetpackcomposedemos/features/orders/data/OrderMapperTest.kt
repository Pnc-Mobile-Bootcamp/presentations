package com.pnc.jetpackcomposedemos.features.orders.data

import com.pnc.jetpackcomposedemos.testutil.builders.aLineItemDto
import junit.framework.TestCase.assertEquals
import org.junit.Test

class OrderMapperTest {

    @Test
    fun `given two lines with the same order number, when mapping, then produces one order with both line items`() {

        // create a collection of OrderLineItemDto
        // (only need to specify the properties that are relevant to this test)
        val dtos = listOf(
            aLineItemDto(id = 1, orderNumber = 5, productName = "Lamp"),
            aLineItemDto(id = 2, orderNumber = 5, productName = "Shade")
        )

        val orders = OrderMapper.mapToDomain(dtos)

        assertEquals(1, orders.size)
        assertEquals(listOf("Lamp", "Shade"), orders.first().items.map { it.productName })
    }

    @Test
    fun `given two lines with different order numbers, when mapping, then produces two orders`() {

        val dtos = listOf(
            aLineItemDto(id = 1, orderNumber = 5, productName = "Lamp"),
            aLineItemDto(id = 2, orderNumber = 6, productName = "Shade")
        )

        val orders = OrderMapper.mapToDomain(dtos)

        assertEquals(2, orders.size)
    }

}