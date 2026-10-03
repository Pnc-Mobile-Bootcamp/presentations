package com.pnc.jetpackcomposedemos.features.artists.domain

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.pnc.jetpackcomposedemos.features.artists.presentation.composables.ArtistDirectory
import com.pnc.jetpackcomposedemos.features.artists.presentation.viewmodels.ArtistDirectoryViewModel

@Composable
fun ArtistDirectoryRoute(
    onBack: () -> Unit = {},
    onArtistSelected: (Int) -> Unit = {},
    useTwoPaneLayout: Boolean,
    viewModel: ArtistDirectoryViewModel = viewModel()
) {
    // get an observable copy of the state inside our viewModel
    val uiState by viewModel.uiState.collectAsState()

    ArtistDirectory(
        state = uiState,
        useTwoPaneLayout = useTwoPaneLayout,
        onBack = onBack,
        onArtistSelected = {
            viewModel.setSelectedArtist(it)
            onArtistSelected(it)
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