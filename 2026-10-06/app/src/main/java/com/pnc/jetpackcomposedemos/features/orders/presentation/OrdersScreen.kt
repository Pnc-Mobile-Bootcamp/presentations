package com.pnc.jetpackcomposedemos.features.orders.presentation

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.pnc.jetpackcomposedemos.core.LoadableState

@Composable
fun OrdersScreen(
    useTwoPaneLayout: Boolean,
    viewModel: OrdersViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadData()
    }

    when (uiState) {
        is LoadableState.Loading -> CircularProgressIndicator()
        is LoadableState.Empty -> Text("No orders found")
        is LoadableState.Error -> {
            val errorMessage = (uiState as LoadableState.Error).message
            Text(
                "Error: $errorMessage",
                color = MaterialTheme.colorScheme.error
            )
        }
        is LoadableState.Success -> {
            val orders = (uiState as LoadableState.Success).data

            OrdersList(
                orders = orders,
                useTwoPaneLayout = useTwoPaneLayout
            )
        }
    }
}

