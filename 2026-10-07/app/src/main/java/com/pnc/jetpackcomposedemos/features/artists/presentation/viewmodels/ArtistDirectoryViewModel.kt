package com.pnc.jetpackcomposedemos.features.artists.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pnc.jetpackcomposedemos.features.artists.domain.Artist
import com.pnc.jetpackcomposedemos.features.artists.domain.GetAvailableArtistsUseCase
import com.pnc.jetpackcomposedemos.features.artists.presentation.state.ArtistDirectoryState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ArtistDirectoryViewModel @Inject constructor(
    private val getAvailableArtists: GetAvailableArtistsUseCase
): ViewModel() {

    // keep the actual mutable state value private
    private val _uiState = MutableStateFlow(
        ArtistDirectoryState()
    )

    // expose (publicly) a read-only view of the state object
    val uiState: StateFlow<ArtistDirectoryState> = _uiState.asStateFlow()

    fun loadArtists() {
        // call an async data loading method and we are not in an async method
        viewModelScope.launch {
            val artists = getAvailableArtists()
            _uiState.update { currentState ->
                currentState.copy(
                    artists = artists
                )
            }
        }
    }

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