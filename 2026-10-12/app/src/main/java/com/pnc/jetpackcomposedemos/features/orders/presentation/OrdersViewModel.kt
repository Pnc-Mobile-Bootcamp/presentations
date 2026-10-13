package com.pnc.jetpackcomposedemos.features.orders.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pnc.jetpackcomposedemos.core.LoadableState
import com.pnc.jetpackcomposedemos.features.orders.domain.GetOrdersUseCase
import com.pnc.jetpackcomposedemos.features.orders.domain.Order
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrdersViewModel @Inject constructor(
    private val getOrders: GetOrdersUseCase
) : ViewModel() {

    private val _uiState: MutableStateFlow<LoadableState<List<Order>>> =
         MutableStateFlow(LoadableState.Loading)

    val uiState: StateFlow<LoadableState<List<Order>>> = _uiState.asStateFlow()

    fun loadData() {
        viewModelScope.launch {
            try {
                val orders = getOrders()
                _uiState.value = LoadableState.Success(orders)
            } catch (e: Exception) {
                _uiState.value = LoadableState.Error(e.message ?: "Unknown error")
            }
        }
    }
}



