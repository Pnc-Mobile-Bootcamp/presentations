package com.pnc.jetpackcomposedemos.features.artists.presentation

import com.pnc.jetpackcomposedemos.features.artists.data.CountingArtistDataSource
import com.pnc.jetpackcomposedemos.features.artists.domain.AsyncRepository
import com.pnc.jetpackcomposedemos.testutil.MainDispatcherRule
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.currentTime
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import kotlin.system.measureTimeMillis

class AsyncViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `does not load artists until ten seconds have passed`() = runTest {
        val dataSource = CountingArtistDataSource()
        val viewModel = AsyncViewModel(AsyncRepository(dataSource))

        val realMillis = measureTimeMillis {
            viewModel.loadArtists()
            advanceTimeBy(9_999)
        }
        println("real time: $realMillis ms, virtual time: $currentTime ms")

        assertEquals(0, dataSource.calls)
    }


}