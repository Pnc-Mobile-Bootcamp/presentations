package com.pnc.jetpackcomposedemos.features.artists.presentation.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.pnc.jetpackcomposedemos.features.artists.domain.Artist

@Composable
fun ArtistList(
    artists: List<Artist>,
    onArtistSelected: (Int) -> Unit,
    innerPadding: PaddingValues
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(
            items = artists,
            key = { artist -> artist.id }
        ) { artist ->
            ArtistCard(
                artist = artist,
                onClick = {
                    onArtistSelected(artist.id)
                }
            )
        }
    }

}