package com.pnc.jetpackcomposedemos.features.artists.presentation.viewmodels

import androidx.lifecycle.SavedStateHandle
import com.pnc.jetpackcomposedemos.features.artists.data.FakeArtistRepository
import com.pnc.jetpackcomposedemos.features.artists.domain.GetAvailableArtistsUseCase
import com.pnc.jetpackcomposedemos.features.artists.domain.RefreshArtistsUseCase
import com.pnc.jetpackcomposedemos.testutil.MainDispatcherRule
import junit.framework.TestCase.assertEquals
import org.junit.Rule
import org.junit.Test

class ArtistDirectoryViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private fun viewModelWith(savedState: SavedStateHandle = SavedStateHandle()):
            ArtistDirectoryViewModel {
                val repository = FakeArtistRepository()
                return ArtistDirectoryViewModel(
                    getAvailableArtists = GetAvailableArtistsUseCase(repository),
                    refreshArtists = RefreshArtistsUseCase(repository),
                    savedStateHandle = savedState,
                )
            }


    @Test
    fun `filters saved before the process was killed are shown again`() {

        // create the saved state that Android would give to our view model
        val restored = SavedStateHandle(mapOf(
            ArtistDirectoryViewModel.GENRE_KEY to "Rock",
            ArtistDirectoryViewModel.LOCATION_KEY to "Miami"
        ))

        val viewModel = viewModelWith(restored)

        assertEquals("Rock", viewModel.uiState.value.genreFilter)
        assertEquals("Miami", viewModel.uiState.value.locationFilter)

    }

    @Test
    fun `changing a filter writes it to the saved state`() {

        val savedState = SavedStateHandle()
        val viewModel = viewModelWith(savedState)

        viewModel.updateGenreFilter("Pop")
        viewModel.updateLocationFilter("Chicago")

        assertEquals("Pop", savedState.get<String>(ArtistDirectoryViewModel.GENRE_KEY))
        assertEquals("Chicago", savedState.get<String>(ArtistDirectoryViewModel.LOCATION_KEY))

    }

    @Test
    fun `a view model with nothing saved starts with empty filters`() {

        val viewModel = viewModelWith()

        assertEquals("", viewModel.uiState.value.genreFilter)
        assertEquals("", viewModel.uiState.value.locationFilter)

    }

}


