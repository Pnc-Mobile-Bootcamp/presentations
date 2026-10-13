package com.pnc.jetpackcomposedemos.features.artists.domain

import kotlinx.coroutines.flow.Flow

interface ArtistRepository {

    fun observeArtists(): Flow<List<Artist>>
    fun observeArtist(id: Int): Flow<Artist?>

    suspend fun refreshArtists()
    suspend fun refreshArtist(id: Int)

}


