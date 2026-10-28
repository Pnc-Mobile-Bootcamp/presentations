package com.pnc.jetpackcomposedemos.features.artists.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pnc.jetpackcomposedemos.features.artists.domain.Artist
import com.pnc.jetpackcomposedemos.features.artists.domain.GetAvailableArtistsUseCase
import com.pnc.jetpackcomposedemos.features.artists.domain.RefreshArtistsUseCase
import com.pnc.jetpackcomposedemos.features.artists.presentation.state.ArtistDirectoryState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlinx.coroutines.CancellationException

@HiltViewModel
class ArtistDirectoryViewModel @Inject constructor(
    private val getAvailableArtists: GetAvailableArtistsUseCase,
    private val refreshArtists: RefreshArtistsUseCase
): ViewModel() {

    // keep the actual mutable state value private
    private val _uiState = MutableStateFlow(
        ArtistDirectoryState()
    )

    // expose (publicly) a read-only view of the state object
    val uiState: StateFlow<ArtistDirectoryState> = _uiState.asStateFlow()

    init {
        // establish a subscription to the observable collection
        // of artists from our Room db
        viewModelScope.launch {
            // by subscribing (collecting) inside the viewModelScope
            // the subscription will be automatically released and disposed
            // when the viewModel is destroyed (i.e. when the user
            // navigates away from this screen)
            getAvailableArtists().collect { artists ->
                _uiState.update { currentState ->
                    currentState.copy(
                        artists = artists
                    )
                }
            }
        }
    }

    // loadArtists does not get the data for the view,
    // instead it refreshes the local Room db
    fun loadArtists() {
        // call an async data loading method and we are not in an async method
        if (_uiState.value.isRefreshing) return

        viewModelScope.launch {
            _uiState.update { currentState ->
                currentState.copy(
                    isRefreshing = true,
                    errorMessage = null
                )
            }

            try {
                refreshArtists()
                _uiState.update { it.copy(isRefreshing = false) }
            }
            catch (e: CancellationException) {
                // NEVER swallow a CancellationException
                // doing so can orphan the coroutine (suspendables)
                throw e
            }
            catch (e: Exception) {
                // cached artists already on screen, they can stay there
                // we just need to surface the error
                _uiState.update {
                    it.copy(
                        isRefreshing = false,
                        errorMessage = e.message ?: "Couldn't refresh artists"
                    )
                }
            }

        }
    }

    fun dismissError() {
        _uiState.update { it.copy(errorMessage = null) }
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