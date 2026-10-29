package com.pnc.jetpackcomposedemos.features.orders.presentation

import androidx.compose.foundation.layout.ExperimentalGridApi
import androidx.compose.foundation.layout.Grid
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.pnc.jetpackcomposedemos.R
import com.pnc.jetpackcomposedemos.features.orders.domain.Order
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalGridApi::class)
@Composable
fun OrderLines(
    order: Order
) {
    val formatter = NumberFormat.getCurrencyInstance(Locale.US)

    Grid(
        modifier = Modifier.padding(4.dp),
        config = {
            column(5.fr)
            column(1.fr)
            column(2.fr)
            column(2.fr)
        }
    ) {
        Text(stringResource(R.string.order_col_product), fontWeight = FontWeight.Bold)
        Text(stringResource(R.string.order_col_qty), fontWeight = FontWeight.Bold)
        Text(stringResource(R.string.order_col_unit_price), fontWeight = FontWeight.Bold)
        Text(stringResource(R.string.order_col_total), fontWeight = FontWeight.Bold)

        order.items.forEach { item ->
            Text(item.productName)
            Text(item.orderQty.toString())
            Text(formatter.format(item.unitPrice))
            Text(formatter.format(item.lineTotal))
        }

    }

}


