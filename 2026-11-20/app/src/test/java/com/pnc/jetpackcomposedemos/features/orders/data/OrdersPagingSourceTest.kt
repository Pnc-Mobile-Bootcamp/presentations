package com.pnc.jetpackcomposedemos.features.orders.data


import androidx.paging.PagingSource
import com.pnc.jetpackcomposedemos.features.auth.data.storage.FakeTokenStore
import com.pnc.jetpackcomposedemos.features.auth.domain.SessionManager
import com.pnc.jetpackcomposedemos.features.auth.domain.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.IOException

// A server with [total] orders that answers page/pageSize the way the real one does. Newest numbers last.
class FakePagedOrdersDataSource(private val total: Int) : MyOrdersDataSource {
    var failure: Throwable? = null
    val requestedPages = mutableListOf<Int>()

    override suspend fun getOrders(customerId: Int, page: Int, pageSize: Int): MyOrderResponseDto {
        failure?.let { throw it }
        requestedPages += page
        val from = (page - 1) * pageSize
        val numbers = (from + 1..minOf(from + pageSize, total)).toList()
        return MyOrderResponseDto(
            customerId = customerId, page = page, pageSize = pageSize, totalOrders = total,
            orders = numbers.map { number ->
                MyOrderDto(
                    orderNumber = number, orderReference = "SO$number", orderDate = "2026-01-01T00:00:00",
                    shipDate = null, status = 5, statusName = "Shipped", subTotal = 10.0, taxAmt = 1.0,
                    freight = 1.0, totalDue = 12.0, lines = emptyList()
                )
            }
        )
    }
}

class OrdersPagingSourceTest {

    private val session = SessionManager(FakeTokenStore(), CoroutineScope(Dispatchers.Unconfined)).apply {
        signIn(User(id = "11000", username = "Cara Cole", token = "t"))
    }

    private fun refresh(key: Int? = null, loadSize: Int = 20) =
        PagingSource.LoadParams.Refresh(key = key, loadSize = loadSize, placeholdersEnabled = false)

    @Test
    fun `the first page holds one page of orders and points to page 2`() = runTest {
        val source = OrdersPagingSource(FakePagedOrdersDataSource(total = 45), session, pageSize = 20)

        val page = source.load(refresh()) as PagingSource.LoadResult.Page

        assertEquals(20, page.data.size)
        assertEquals(1, page.data.first().id)
        assertNull(page.prevKey)
        assertEquals(2, page.nextKey)
    }

    @Test
    fun `the last page has no next key`() = runTest {
        val source = OrdersPagingSource(FakePagedOrdersDataSource(total = 45), session, pageSize = 20)

        val page = source.load(refresh(key = 3)) as PagingSource.LoadResult.Page

        assertEquals(5, page.data.size)
        assertEquals(2, page.prevKey)
        assertNull(page.nextKey)
    }

    @Test
    fun `a total that fills the page exactly ends there`() = runTest {
        val source = OrdersPagingSource(FakePagedOrdersDataSource(total = 20), session, pageSize = 20)

        val page = source.load(refresh()) as PagingSource.LoadResult.Page

        assertNull(page.nextKey)
    }

    @Test
    fun `a network failure becomes an Error result the UI can retry`() = runTest {
        val dataSource = FakePagedOrdersDataSource(total = 45).apply { failure = IOException("network down") }
        val source = OrdersPagingSource(dataSource, session, pageSize = 20)

        val result = source.load(refresh())

        assertTrue(result is PagingSource.LoadResult.Error)
        assertEquals("network down", (result as PagingSource.LoadResult.Error).throwable.message)
    }

    @Test
    fun `nobody signed in is an Error result and asks the server for nothing`() = runTest {
        val signedOut = SessionManager(FakeTokenStore(), CoroutineScope(Dispatchers.Unconfined))
        val dataSource = FakePagedOrdersDataSource(total = 45)
        val source = OrdersPagingSource(dataSource, signedOut, pageSize = 20)

        val result = source.load(refresh())

        assertTrue(result is PagingSource.LoadResult.Error)
        assertTrue(dataSource.requestedPages.isEmpty())
    }
}