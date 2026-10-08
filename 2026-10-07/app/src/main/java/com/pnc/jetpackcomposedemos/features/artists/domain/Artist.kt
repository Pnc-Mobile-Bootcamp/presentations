package com.pnc.jetpackcomposedemos.features.artists.domain

data class Artist (
    val id: Int,
    val name: String,
    val genre: String,
    val location: String,
    val imageUrl: String,
    val description: String,
    val isAvailable: Boolean
)

