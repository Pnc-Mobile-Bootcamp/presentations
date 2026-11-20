package com.pnc.jetpackcomposedemos.features.artists.presentation.state

import com.pnc.jetpackcomposedemos.features.artists.domain.Artist

data class ArtistDirectoryState(
    val artists: List<Artist> = emptyList(),
    val searchQuery: String = "",
    val genreFilter: String = "",
    val locationFilter: String = "",
    val showFilter: Boolean = false,
    val selectedArtistId: Int? = null,
    val isRefreshing: Boolean = false,
    val errorMessage: String? = null
) {
    val displayedArtists: List<Artist>
        get() = artists.filter {
            val matchesSearch = searchQuery.isBlank() || it.name.contains(searchQuery, ignoreCase = true)
            val matchesGenre = genreFilter == "" || it.genre.contains(genreFilter, ignoreCase = true)
            val matchesLocation = locationFilter == "" || it.location.contains(locationFilter, ignoreCase = true)

            matchesSearch && matchesGenre && matchesLocation
        }

    val selectedArtist: Artist?
        get() = artists.find { it.id == selectedArtistId }

}