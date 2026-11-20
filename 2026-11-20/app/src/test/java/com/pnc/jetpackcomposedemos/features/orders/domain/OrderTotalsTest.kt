package com.pnc.jetpackcomposedemos.features.orders.domain

import com.pnc.jetpackcomposedemos.testutil.builders.anOrder
import org.junit.Assert
import org.junit.Test
import java.time.LocalDateTime
import java.time.YearMonth

class OrderTotalsTest {

    private fun orderOn(year: Int, month: Int, vararg lineTotals: Double) = anOrder {
        orderDate = LocalDateTime.of(year, month, 15, 0, 0)
        lineTotals.forEach { item(unitPrice = it) }
    }

    @Test
    fun `no orders gives no totals`() {
        Assert.assertTrue(emptyList<Order>().totalsByMonth().isEmpty())
    }

    @Test
    fun `lines from different orders in the same month are added together`() {
        val totals = listOf(
            orderOn(2026, 3, 10.0, 5.0),
            orderOn(2026, 3, 100.0)
        ).totalsByMonth()

        Assert.assertEquals(listOf(MonthTotal(YearMonth.of(2026, 3), 115.0)), totals)
    }

    @Test
    fun `months come out oldest first no matter the order they arrive in`() {
        val totals = listOf(
            orderOn(2026, 5, 1.0),
            orderOn(2026, 3, 1.0),
            orderOn(2026, 4, 1.0)
        ).totalsByMonth()

        Assert.assertEquals(listOf(3, 4, 5), totals.map { it.month.monthValue })
    }

    @Test
    fun `only the most recent months are kept`() {
        val orders = (1..9).map { month -> orderOn(2026, month, 1.0) }

        val totals = orders.totalsByMonth(months = 6)

        Assert.assertEquals(listOf(4, 5, 6, 7, 8, 9), totals.map { it.month.monthValue })
    }
}