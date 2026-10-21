package com.pnc.jetpackcomposedemos.features.artists.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pnc.jetpackcomposedemos.core.LoadableState
import com.pnc.jetpackcomposedemos.features.artists.domain.Artist
import com.pnc.jetpackcomposedemos.features.artists.domain.GetArtistUseCase
import com.pnc.jetpackcomposedemos.features.artists.domain.RefreshArtistUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ArtistDetailsViewModel @Inject constructor(
    private val getArtist: GetArtistUseCase,
    private val refreshArtist: RefreshArtistUseCase
): ViewModel() {

    private val _uiState: MutableStateFlow<LoadableState<Artist>> = MutableStateFlow(
        LoadableState.Loading
    )

    val uiState: StateFlow<LoadableState<Artist>> = _uiState.asStateFlow()

    private var loadJob: Job? = null
    private var loadedId: Int? = null

    fun loadArtist(id: Int) {
        // Don't restart (and flash loading) if this id is already being observed
        if (id == loadedId && loadJob?.isActive == true) return

        loadedId = id
        loadJob?.cancel()
        loadJob = viewModelScope.launch {
            _uiState.value = LoadableState.Loading
            val hadCache = getArtist(id).first() != null

            // refresh the artist data in the background
            launch {
                try {
                    refreshArtist(id)
                }
                catch (e: CancellationException) {
                    throw e
                }
                catch (e: Exception) {
                    // with a cached copy on screen, a failed refresh is silent
                    if (!hadCache) {
                        _uiState.value = LoadableState.Error(
                            e.message ?: "Couldn't load artist"
                        )
                    }
                }
            }

            // establish our subscription
            getArtist(id).collect { artist ->
                when {
                    artist != null -> _uiState.value = LoadableState.Success(artist)
                    hadCache -> _uiState.value = LoadableState.Empty // temporary state while the refresh is still underway
                    // no other useful cases - stay in loading state
                    // until the refresh either writes to the db or fails
                }
            }

        }
    }

}


