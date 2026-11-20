package com.pnc.jetpackcomposedemos.features.artists.data

import com.pnc.jetpackcomposedemos.features.artists.domain.Artist
import com.pnc.jetpackcomposedemos.features.artists.domain.ArtistRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DefaultArtistRepository @Inject constructor(
    private val dataSource: ArtistDataSource
): ArtistRepository {

    override fun observeArtists(): Flow<List<Artist>> = flow {
        emit( dataSource.getArtists().map {
            ArtistMapper.mapToDomain(it)
        })
    }

    override fun observeArtist(id: Int): Flow<Artist?> = flow {
        emit(ArtistMapper.mapToDomain(dataSource.getArtist(id)))
    }

    // Nothing is cached in this repository,
    // so there is nothing to refresh
    override suspend fun refreshArtists() = Unit
    override suspend fun refreshArtist(id: Int) = Unit


}


