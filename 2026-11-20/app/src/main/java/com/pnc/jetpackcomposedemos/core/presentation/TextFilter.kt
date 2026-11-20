package com.pnc.jetpackcomposedemos.core.presentation

import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable

@Composable
fun TextFilter(
    label: String,
    filter: String,
    onFilterChange: (String) -> Unit
) {

    TextField(
        value = filter,
        onValueChange = onFilterChange,
        label = {
            Text("$label:")
        }
    )

}

