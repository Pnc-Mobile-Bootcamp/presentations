package com.pnc.jetpackcomposedemos.features.orders.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.pnc.jetpackcomposedemos.R
import com.pnc.jetpackcomposedemos.features.orders.domain.MonthTotal
import com.pnc.jetpackcomposedemos.ui.theme.LocalSpacing
import java.text.NumberFormat
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale
import androidx.compose.ui.platform.LocalLocale

@Composable
fun OrdersChartCard(
    totals: List<MonthTotal>,
    modifier: Modifier = Modifier
) {
    if (totals.isEmpty()) return

    val currency = remember { NumberFormat.getCurrencyInstance(Locale.US) }
    val locale = LocalLocale.current.platformLocale

    val bars = remember(totals) {
        totals.map { Bar(
            label = it.month.month.getDisplayName(TextStyle.SHORT, locale),
            value = it.total
        ) }
    }
    var selectedIndex by rememberSaveable(totals.size) {
        mutableStateOf<Int?>(null)
    }

    val monthFormat = remember { DateTimeFormatter.ofPattern("MMMM yyyy", locale) }
    val heading = selectedIndex?.takeIf { it in totals.indices }?.let { index ->
        stringResource(
            R.string.orders_chart_selected,
            totals[index].month.format(monthFormat),
            currency.format(totals[index].total)
        )
    } ?: stringResource(R.string.orders_chart_title)

    Card(
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(LocalSpacing.current.large),
            verticalArrangement = Arrangement.spacedBy(LocalSpacing.current.medium)
        ) {
            Text(
                text = heading,
                style = MaterialTheme.typography.titleMedium
            )
            BarChart(
                bars = bars,
                selectedIndex = selectedIndex,
                onSelect = { selectedIndex = it },
                summary = stringResource(R.string.orders_chart_summary, totals.size)
            )
        }
    }

}




