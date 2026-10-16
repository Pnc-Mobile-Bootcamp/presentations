package com.pnc.jetpackcomposedemos.features.artists.data

class CountingArtistDataSource: ArtistDataSource {

    var calls = 0
        private set

    override suspend fun getArtists(): List<ArtistDto> {
        calls++
        return emptyList()
    }

    override suspend fun getArtist(id: Int): ArtistDto = error("not used in these tests")


}

