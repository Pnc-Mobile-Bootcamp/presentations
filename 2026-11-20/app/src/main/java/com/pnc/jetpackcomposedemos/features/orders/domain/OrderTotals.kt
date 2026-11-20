package com.pnc.jetpackcomposedemos.features.orders.domain

import java.time.YearMonth

data class MonthTotal(
    val month: YearMonth,
    val total: Double
)


// convenience extension method to compute the sum of each line item
// of a list of orders, per calendar month, oldest first
// (potentially limited by the most recent [months])
fun List<Order>.totalsByMonth(months: Int = 6): List<MonthTotal> =
    groupBy { YearMonth.from(it.orderDate) }
        .map { (month, ordersInMonth) ->
            MonthTotal(month, ordersInMonth.sumOf {order -> order.items.sumOf { it.lineTotal }} )
        }
        .sortedBy { it.month }
        .takeLast(months)



