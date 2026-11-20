package com.pnc.jetpackcomposedemos.features.artists.domain

import com.pnc.jetpackcomposedemos.features.artists.data.DefaultArtistRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetArtistUseCase @Inject constructor(
    private val artistRepository: ArtistRepository
) {

    operator fun invoke(id: Int): Flow<Artist?> {
        return artistRepository.observeArtist(id)
    }

}



