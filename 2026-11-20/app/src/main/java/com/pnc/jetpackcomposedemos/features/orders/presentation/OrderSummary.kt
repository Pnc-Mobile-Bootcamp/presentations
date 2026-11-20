package com.pnc.jetpackcomposedemos.features.orders.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.pnc.jetpackcomposedemos.R
import com.pnc.jetpackcomposedemos.features.orders.domain.Order
import java.time.format.DateTimeFormatter

@Composable
fun OrderSummary(
    order: Order
) {
    val formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy")
    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(stringResource(R.string.order_number, order.id))
        Text(stringResource(R.string.order_customer, order.firstName, order.lastName))
        Text(stringResource(R.string.order_date, order.orderDate.format(formatter)))
        Text(stringResource(R.string.order_ship_date, order.shipDate.format(formatter)))
    }
}


