package com.pnc.jetpackcomposedemos.features.artists.presentation.composables

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.viewmodel.compose.viewModel
import com.pnc.jetpackcomposedemos.features.artists.presentation.viewmodels.ArtistDirectoryViewModel

@Composable
fun ArtistDirectoryRoute(
    onBack: () -> Unit = {},
    onArtistSelected: (Int) -> Unit = {},
    useTwoPaneLayout: Boolean,
    viewModel: ArtistDirectoryViewModel = hiltViewModel()
) {
    // get an observable copy of the state inside our viewModel
    val uiState by viewModel.uiState.collectAsState()

    LifecycleEventEffect(Lifecycle.Event.ON_START) {
        viewModel.loadArtists()
    }

    ArtistDirectory(
        state = uiState,
        useTwoPaneLayout = useTwoPaneLayout,
        onBack = onBack,
        onArtistSelected = {
            viewModel.setSelectedArtist(it)
            if (!useTwoPaneLayout) {
                onArtistSelected(it)
            }
        },
        onGenreChange = {
            viewModel.updateGenreFilter(it)
        },
        onLocationChange = {
            viewModel.updateLocationFilter(it)
        },
        onShowFilterChange = {
            viewModel.updateShowFilter(it)
        }
    )

}