package com.pnc.jetpackcomposedemos.features.artists.data

interface ArtistDataSource {
    suspend fun getArtists(): List<ArtistDto>
}

