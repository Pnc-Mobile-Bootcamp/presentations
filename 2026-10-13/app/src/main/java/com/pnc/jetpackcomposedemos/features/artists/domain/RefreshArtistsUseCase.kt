package com.pnc.jetpackcomposedemos.features.artists.domain

import javax.inject.Inject

class RefreshArtistsUseCase @Inject constructor(
    private val repository: ArtistRepository
) {

    suspend operator fun invoke() {
        repository.refreshArtists()
    }

}

