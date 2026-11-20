package com.pnc.jetpackcomposedemos.features.orders.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.pnc.jetpackcomposedemos.features.orders.data.PagedOrdersRepository
import com.pnc.jetpackcomposedemos.features.orders.domain.Order
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class PagedOrdersViewModel @Inject constructor(
    repository: PagedOrdersRepository
): ViewModel() {

    val orders: Flow<PagingData<Order>> = repository.pagedOrders().cachedIn(viewModelScope)
    // cachedIn keeps the loaded page of data alive across rotation
    // so the list does not reload from page 1 again

}



