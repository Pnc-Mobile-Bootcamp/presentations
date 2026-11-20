package com.pnc.jetpackcomposedemos.testutil.builders

import com.pnc.jetpackcomposedemos.features.artists.domain.Artist

fun anArtist(
    id: Int = 1,
    name: String = "Test Artist",
    genre: String = "Rock",
    location: String = "New York, NY",
    imageUrl: String = "",
    description: String = "",
    isAvailable: Boolean = true
) = Artist(id, name, genre, location, imageUrl, description, isAvailable)

