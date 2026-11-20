package com.pnc.jetpackcomposedemos.features.orders.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.pnc.jetpackcomposedemos.features.orders.domain.Order

@Composable
fun OrderCard(
    order: Order,
    useTwoPaneLayout: Boolean
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        if (useTwoPaneLayout) {
            Row(
                modifier = Modifier.fillMaxWidth()
                    // the following is necessary for the VerticalDivider
                    // to know the height of the row, so it can display
                    .height(IntrinsicSize.Min),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(modifier = Modifier.weight(2f).padding(12.dp)) {
                    OrderSummary(order = order)
                }
                VerticalDivider()
                Box(modifier = Modifier.weight(3f).padding(12.dp)) {
                    OrderLines(order = order)
                }
            }
        } else {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(modifier = Modifier.padding(12.dp)) {
                    OrderSummary(order = order)
                }
                HorizontalDivider()
                Box(modifier = Modifier.padding(12.dp)) {
                    OrderLines(order = order)
                }
            }
        }
    }
}



