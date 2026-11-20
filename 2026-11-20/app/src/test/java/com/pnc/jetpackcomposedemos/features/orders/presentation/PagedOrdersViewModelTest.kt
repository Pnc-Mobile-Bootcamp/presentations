package com.pnc.jetpackcomposedemos.features.orders.presentation


import androidx.paging.testing.asSnapshot
import com.pnc.jetpackcomposedemos.features.auth.data.storage.FakeTokenStore
import com.pnc.jetpackcomposedemos.features.auth.domain.SessionManager
import com.pnc.jetpackcomposedemos.features.auth.domain.User
import com.pnc.jetpackcomposedemos.features.orders.data.FakePagedOrdersDataSource
import com.pnc.jetpackcomposedemos.features.orders.data.PagedOrdersRepository
import com.pnc.jetpackcomposedemos.testutil.MainDispatcherRule
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class PagedOrdersViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val session = SessionManager(FakeTokenStore(), CoroutineScope(Dispatchers.Unconfined)).apply {
        signIn(User(id = "11000", username = "Cara Cole", token = "t"))
    }

    private fun viewModelWith(total: Int, dataSource: FakePagedOrdersDataSource = FakePagedOrdersDataSource(total)) =
        PagedOrdersViewModel(PagedOrdersRepository(dataSource, session))

    @Test
    fun `before anything is scrolled the whole list has not been loaded`() = runTest {
        val dataSource = FakePagedOrdersDataSource(total = 100)
        val viewModel = viewModelWith(100, dataSource)

        val loaded = viewModel.orders.asSnapshot { }

        // Page 1 first. Page 2 may follow because Paging prefetches ahead of the viewport, but not all five pages.
        assertEquals(1, dataSource.requestedPages.first())
        assertTrue(dataSource.requestedPages.size < 5)
        assertTrue(loaded.size < 100)
    }

    @Test
    fun `scrolling toward the end loads further pages`() = runTest {
        val dataSource = FakePagedOrdersDataSource(total = 100)
        val viewModel = viewModelWith(100, dataSource)

        val loaded = viewModel.orders.asSnapshot { scrollTo(index = 35) }

        assertTrue("expected more than two pages, got ${loaded.size}", loaded.size > 2 * PagedOrdersRepository.PAGE_SIZE)
        assertTrue(dataSource.requestedPages.containsAll(listOf(1, 2, 3)))
    }
}



