package com.pnc.jetpackcomposedemos.features.artists.presentation.viewmodels

import androidx.lifecycle.ViewModel
import com.pnc.jetpackcomposedemos.features.artists.domain.Artist
import com.pnc.jetpackcomposedemos.features.artists.presentation.state.ArtistDirectoryState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ArtistDirectoryViewModel: ViewModel() {

    // keep the actual mutable state value private
    private val _uiState = MutableStateFlow(
        ArtistDirectoryState(
            artists = Artist.getArtists()
        )
    )

    // expose (publicly) a read-only view of the state object
    val uiState: StateFlow<ArtistDirectoryState> = _uiState.asStateFlow()

    fun updateGenreFilter(genre: String) {

        // this approach is vulnerable to race conditions
        // (usually triggered by async code)
        // so don't use this pattern in a ViewModel
//        _uiState.value = _uiState.value.copy(genreFilter = genre)

        // change the state in a way that can prevent race conditions
        _uiState.update { currentState ->
            currentState.copy(
                genreFilter = genre
            )
        }
    }

    fun updateLocationFilter(location: String) {
        _uiState.update { currentState ->
            currentState.copy(
                locationFilter = location
            )
        }
    }

    fun updateShowFilter(show: Boolean) {
        _uiState.update { currentState ->
            currentState.copy(
                showFilter = show
            )
        }
    }

    fun setSelectedArtist(artistId: Int) {
        _uiState.update { currentState ->
            currentState.copy(
                selectedArtistId = artistId
            )
        }
    }

}