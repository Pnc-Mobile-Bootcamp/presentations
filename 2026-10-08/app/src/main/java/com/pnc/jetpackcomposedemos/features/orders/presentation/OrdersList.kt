package com.pnc.jetpackcomposedemos.features.orders.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.pnc.jetpackcomposedemos.features.orders.domain.Order

@Composable
fun OrdersList(
    orders: List<Order>,
    useTwoPaneLayout: Boolean
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(
            items = orders,
            key = { order: Order -> order.id }
        ) { order: Order ->
            OrderCard(
                order = order,
                useTwoPaneLayout = useTwoPaneLayout
            )
        }
    }
}


