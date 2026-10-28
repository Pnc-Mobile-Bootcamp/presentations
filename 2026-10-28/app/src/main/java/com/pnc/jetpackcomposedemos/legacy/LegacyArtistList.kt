package com.pnc.jetpackcomposedemos.legacy

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.viewinterop.AndroidViewBinding
import androidx.recyclerview.widget.LinearLayoutManager
import com.pnc.jetpackcomposedemos.features.artists.domain.Artist
import com.pnc.jetpackcomposedemos.databinding.LegacyArtistListBinding

@Composable
fun LegacyArtistList(
    artists: List<Artist>,
    onArtistSelected: (Int) -> Unit
) {
    val adapter = remember(artists) {
        ArtistsViewAdapter(
            artists = artists,
            onArtistSelected = onArtistSelected
        )
    }

    AndroidViewBinding(
        factory = LegacyArtistListBinding::inflate
    ) {
        if (artistRecyclerView.layoutManager == null) {
            artistRecyclerView.layoutManager = LinearLayoutManager(root.context)
        }

        if (artistRecyclerView.adapter !== adapter) {
            artistRecyclerView.adapter = adapter
        }
    }

}




