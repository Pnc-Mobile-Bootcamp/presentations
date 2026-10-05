package com.pnc.jetpackcomposedemos.features.artists.data

import com.pnc.jetpackcomposedemos.features.artists.domain.Artist
import com.pnc.jetpackcomposedemos.features.artists.domain.ArtistRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DefaultArtistRepository @Inject constructor(
    private val dataSource: ArtistDataSource
): ArtistRepository {

    override suspend fun getArtists(): List<Artist> {
        return dataSource.getArtists().map {
            ArtistMapper.mapToDomain(it)
        }
    }

    override suspend fun getArtist(id: Int): Artist? {
        return getArtists()
            .firstOrNull {
                it.id == id
            }
    }

}


