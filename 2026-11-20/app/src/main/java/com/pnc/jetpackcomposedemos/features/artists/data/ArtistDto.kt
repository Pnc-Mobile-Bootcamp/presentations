package com.pnc.jetpackcomposedemos.features.artists.data

data class ArtistDto(
    val id: Int,
    val name: String,
    val genre: String,
    val location: String,
    val imageUrl: String,
    val description: String,
    val tags: String
)

