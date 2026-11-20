package com.pnc.jetpackcomposedemos.features.orders.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.pnc.jetpackcomposedemos.features.auth.domain.SessionManager
import com.pnc.jetpackcomposedemos.features.orders.domain.Order
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PagedOrdersRepository @Inject constructor(
    private val dataSource: MyOrdersDataSource,
    private val session: SessionManager
) {
    companion object {
        const val PAGE_SIZE = 5
    }

    fun pagedOrders(pageSize: Int = PAGE_SIZE): Flow<PagingData<Order>> =
        Pager(
            config = PagingConfig(
                pageSize = pageSize,
                initialLoadSize = pageSize,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { OrdersPagingSource(dataSource, session, pageSize) }
        ).flow

}



