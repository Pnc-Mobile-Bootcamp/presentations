package com.pnc.jetpackcomposedemos.features.artists.domain

interface ArtistRepository {

    suspend fun getArtists(): List<Artist>
    suspend fun getArtist(id: Int): Artist?

}


