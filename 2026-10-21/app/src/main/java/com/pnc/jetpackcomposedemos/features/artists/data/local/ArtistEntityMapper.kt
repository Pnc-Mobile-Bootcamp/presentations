package com.pnc.jetpackcomposedemos.features.artists.data.local

import com.pnc.jetpackcomposedemos.features.artists.data.ArtistDto
import com.pnc.jetpackcomposedemos.features.artists.domain.Artist

object ArtistEntityMapper {

    fun toDomain(entity: ArtistEntity): Artist {
        return Artist(
            id = entity.id,
            name = entity.name,
            genre = entity.genre,
            location = entity.location,
            imageUrl = entity.imageUrl,
            description = entity.description,
            isAvailable = entity.isAvailable
        )
    }

    fun toEntity(domain: ArtistDto): ArtistEntity {
        return ArtistEntity(
            id = domain.id,
            name = domain.name,
            genre = domain.genre,
            location = domain.location,
            imageUrl = domain.imageUrl,
            description = domain.description,
            isAvailable = true
        )
    }
}