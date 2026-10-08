package com.pnc.jetpackcomposedemos.features.artists.data

import retrofit2.http.GET
import retrofit2.http.Path

interface RemoteArtistDataSource: ArtistDataSource {

    @GET("talent")
    override suspend fun getArtists(): List<ArtistDto>

    @GET("talent/{artistId}")
    override suspend fun getArtist(
        @Path("artistId") id: Int
    ): ArtistDto?

}