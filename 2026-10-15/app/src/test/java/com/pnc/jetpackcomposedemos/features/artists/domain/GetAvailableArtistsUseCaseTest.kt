package com.pnc.jetpackcomposedemos.features.artists.domain

import com.pnc.jetpackcomposedemos.features.artists.data.FakeArtistRepository
import com.pnc.jetpackcomposedemos.testutil.builders.anArtist
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.flow.toList
import org.junit.Test
import kotlin.time.Duration.Companion.seconds
import app.cash.turbine.test
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import okio.IOException

class GetAvailableArtistsUseCaseTest {

//    @Test
//    fun `toList never finishes on a flow that never completes`() = runTest(timeout = 2.seconds) {
//
//        val repo = FakeArtistRepository(listOf(anArtist()))
//
//        val result = GetAvailableArtistsUseCase(repo)().toList()
//
//    }

    @Test
    fun `emits cached artists filtered to available and sorted by name`() = runTest {
        val repo = FakeArtistRepository(
            listOf(
                anArtist(id = 2, name = "Zed"),
                anArtist(id = 1, name = "Abe"),
                anArtist(id = 3, name = "Hidden", isAvailable = false)
            )
        )

        GetAvailableArtistsUseCase(repo)().test {
            assertEquals(listOf("Abe", "Zed"), awaitItem().map { it.name })
        }   // turbine stops collecting the flow of data
            // and also asserts that nothing is left unconsumed in the flow

    }

    @Test
    fun `emits again when the underlying data changes`() = runTest {

        val repo = FakeArtistRepository(listOf(anArtist(id = 1, name = "Abe")))

        GetAvailableArtistsUseCase(repo)().test {   // turbine starts observing the flow
            assertEquals(listOf("Abe"), awaitItem().map { it.name })

            // change the repo data
            repo.artists.value = listOf(
                anArtist(id = 1, name = "Abe"),
                anArtist(id = 2, name = "Bea")
            )
            assertEquals(listOf("Abe", "Bea"), awaitItem().map { it.name })

            repo.artists.value = listOf(anArtist(id = 1, name = "Abe", isAvailable = false))
            assertEquals(emptyList<Artist>(), awaitItem())
        }

    }

    @Test
    fun `does not emit again when the same artists are pushed`() = runTest {

        val repo = FakeArtistRepository(listOf(anArtist(id = 1)))

        GetAvailableArtistsUseCase(repo)().test {
            skipItems(1)    // we don't care about the initial emission

            repo.artists.value = listOf(anArtist(id = 1))   // equal list, new instance

            expectNoEvents()
        }

    }

    @Test
    fun `propagates a failure from the repository flow`() = runTest {
        // new (to us) syntax to create a fake, on-the-fly, inside our test
        val failing = object : ArtistRepository by FakeArtistRepository() {
            override fun observeArtists(): Flow<List<Artist>> = flow {
                emit(listOf(anArtist(name = "Abe")))
                throw IOException("db closed")
            }
        }
        // object : ArtistRepository by FakeArtistRepository()
        // this is called interface delegation

        GetAvailableArtistsUseCase(failing)().test {

            assertEquals(listOf("Abe"), awaitItem().map { it.name })
            assertEquals("db closed", awaitError().message)

        }

    }

    @Test
    fun `completes when the repository flow completes`() = runTest {

        val finite = object : ArtistRepository by FakeArtistRepository() {
            override fun observeArtists(): Flow<List<Artist>> = flowOf(listOf(anArtist()))
        }

        GetAvailableArtistsUseCase(finite)().test {
            skipItems(1)
            awaitComplete()
        }

    }



}



