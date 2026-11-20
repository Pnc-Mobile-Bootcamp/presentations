package com.pnc.jetpackcomposedemos.features.artists.presentation

import androidx.lifecycle.viewModelScope
import com.pnc.jetpackcomposedemos.features.artists.data.CountingArtistDataSource
import com.pnc.jetpackcomposedemos.features.artists.domain.AsyncRepository
import com.pnc.jetpackcomposedemos.testutil.MainDispatcherRule
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.currentTime
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import kotlin.system.measureTimeMillis

@OptIn(ExperimentalCoroutinesApi::class)
class AsyncViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()


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

    @Test
    fun `loads artists after exactly ten seconds`() = runTest {
        val dataSource = CountingArtistDataSource()
        val viewModel = AsyncViewModel(AsyncRepository(dataSource))

        viewModel.loadArtists()
        runCurrent()        // start the coroutine, but only execute up to the current time
                            // (which is 0, and will not advance automatically)
        assertEquals(0, dataSource.calls)
        assertEquals(0, currentTime)

        advanceTimeBy(9_999)    // moves the clock forward, but does not run tasks
        runCurrent()            // executes the tasks up to the current time
        assertEquals(0, dataSource.calls)   // still 1 millisecond early

        advanceTimeBy(1)
        runCurrent()
        assertEquals(1, dataSource.calls)   // now 10 seconds have passed
        assertEquals(10_000, currentTime)

    }

    @Test
    fun `eventually loads artists`() = runTest {
        val dataSource = CountingArtistDataSource()
        val viewModel = AsyncViewModel(AsyncRepository(dataSource))

        viewModel.loadArtists()

        // suppose we know this is async and takes some amount of time
        // but we either don't know exactly how long, or it may be a variable
        // amount of time, and we don't care exactly how long, we just
        // need to wait until it finishes to make our assertions
        advanceUntilIdle()      // tells the testing dispatcher to fast-forward execute
                                // everything in its queue until it is idle

        assertEquals(1, dataSource.calls)

    }

    @Test
    fun `does not load artists if the view model is cleared during the wait`() = runTest {

        val dataSource = CountingArtistDataSource()
        val viewModel = AsyncViewModel(AsyncRepository(dataSource))

        viewModel.loadArtists()
        advanceTimeBy(5_000)
        viewModel.viewModelScope.cancel()   // what happens when the screen goes away
        advanceUntilIdle()

        assertEquals(0, dataSource.calls)

    }


}


