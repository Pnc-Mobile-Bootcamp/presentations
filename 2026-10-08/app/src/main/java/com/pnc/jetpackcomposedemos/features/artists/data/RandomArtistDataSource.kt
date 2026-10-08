package com.pnc.jetpackcomposedemos.features.artists.data

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RandomArtistDataSource @Inject constructor() : ArtistDataSource {

    override suspend fun getArtists(): List<ArtistDto> {

        return List(100) { index ->
            ArtistDto(
                id = index + 1,
                name = "Artist ${index + 1}",
                genre = listOf("Rock", "Pop", "Jazz", "Acoustic", "Classical").random(),
                location = listOf("New York", "Los Angeles", "Chicago", "Houston", "Miami").random(),
                imageUrl = "/images/fakeimage.jpg",
                description = "Some fake details",
                tags = "tag,tag,tag",
            )
        }

    }

    override suspend fun getArtist(id: Int): ArtistDto? {
        return ArtistDto(
            id = id,
            name = "Artist ${id}",
            genre = listOf("Rock", "Pop", "Jazz", "Acoustic", "Classical").random(),
            location = listOf("New York", "Los Angeles", "Chicago", "Houston", "Miami").random(),
            imageUrl = "/images/fakeimage.jpg",
            description = "Some fake details",
            tags = "tag,tag,tag",
        )
    }


}