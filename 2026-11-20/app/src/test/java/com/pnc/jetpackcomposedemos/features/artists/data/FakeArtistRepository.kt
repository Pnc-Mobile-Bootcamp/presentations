package com.pnc.jetpackcomposedemos.features.artists.data

import com.pnc.jetpackcomposedemos.features.artists.domain.Artist
import com.pnc.jetpackcomposedemos.features.artists.domain.ArtistRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class FakeArtistRepository(
    initial: List<Artist> = emptyList()
): ArtistRepository {

    val artists = MutableStateFlow(initial)

    override fun observeArtists(): Flow<List<Artist>> = artists

    override fun observeArtist(id: Int): Flow<Artist?> = artists.map { artists ->
        artists.firstOrNull { it.id == id }
    }

    // the following are not needed for these tests
    override suspend fun refreshArtists() = Unit
    override suspend fun refreshArtist(id: Int) = Unit

}


