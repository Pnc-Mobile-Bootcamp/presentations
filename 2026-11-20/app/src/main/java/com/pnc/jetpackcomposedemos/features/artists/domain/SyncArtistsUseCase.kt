package com.pnc.jetpackcomposedemos.features.artists.domain

import kotlinx.coroutines.flow.first
import javax.inject.Inject

class SyncArtistsUseCase @Inject constructor(
    private val repository: ArtistRepository
) {
    // refresh the cache from the server and return how many artists
    // are new since the last sync.
    // the first fill of an empty cache should report 0  -
    // everything is new then, but that is hardly news
    suspend operator fun invoke(): Int {
        val before = repository.observeArtists().first().map { it.id }.toSet()
        repository.refreshArtists()
        val after = repository.observeArtists().first()

        return if (before.isEmpty()) 0 else after.count { it.id !in before }
    }

}