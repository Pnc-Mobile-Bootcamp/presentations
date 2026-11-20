package com.pnc.jetpackcomposedemos.features.artists.domain

import com.pnc.jetpackcomposedemos.features.artists.data.DefaultArtistRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetAvailableArtistsUseCase @Inject constructor(
    private val artistRepository: ArtistRepository
) {

    operator fun invoke(): Flow<List<Artist>> {
        return artistRepository
            .observeArtists()
            .map { artists ->
                artists
                    .filter { it.isAvailable }
                    .sortedBy { it.name }
            }
    }

}

