package com.pnc.jetpackcomposedemos.features.artists.domain

import com.pnc.jetpackcomposedemos.features.artists.data.DefaultArtistRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetAvailableArtistsUseCase @Inject constructor(
    private val artistRepository: ArtistRepository
) {

    suspend operator fun invoke(): List<Artist> {
        return artistRepository
            .getArtists()
            .filter { it.isAvailable }
            .sortedBy { it.name }
    }

}