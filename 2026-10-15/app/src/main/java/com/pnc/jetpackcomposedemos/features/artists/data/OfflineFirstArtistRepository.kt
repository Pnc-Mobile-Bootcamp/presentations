package com.pnc.jetpackcomposedemos.features.artists.data

import com.pnc.jetpackcomposedemos.features.artists.data.local.ArtistDao
import com.pnc.jetpackcomposedemos.features.artists.data.local.ArtistEntityMapper
import com.pnc.jetpackcomposedemos.features.artists.domain.Artist
import com.pnc.jetpackcomposedemos.features.artists.domain.ArtistRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OfflineFirstArtistRepository @Inject constructor(
    private val artistDao: ArtistDao,
    private val remote: ArtistDataSource
): ArtistRepository {

    override fun observeArtists(): Flow<List<Artist>> =
        artistDao.observeArtists().map { list ->
            list.map { entity ->
                ArtistEntityMapper.toDomain(entity)
            }
        }

    override fun observeArtist(id: Int): Flow<Artist?>  =
        artistDao.observeArtist(id).map { entity ->
            entity?.let { ArtistEntityMapper.toDomain(it) }
        }

    override suspend fun refreshArtists() {
        val entities = remote.getArtists().map {
            ArtistEntityMapper.toEntity(it)
        }
        artistDao.replaceAll(entities)
    }

    override suspend fun refreshArtist(id: Int) {
        val entity = ArtistEntityMapper.toEntity(remote.getArtist(id))
        artistDao.upsertArtists(listOf(entity))
    }

//    suspend fun createArtist(artist: Artist) {
//        // POST the data to the web API
//        refreshArtists()
//    }

}


