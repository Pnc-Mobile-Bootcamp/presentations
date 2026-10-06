package com.pnc.jetpackcomposedemos.features.artists.data

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HardCodedArtistDataSource @Inject constructor() : ArtistDataSource {

    override suspend fun getArtists(): List<ArtistDto> {

        return listOf(
            ArtistDto(
                id = 100,
                name = "High Voltage",
                genre = "Rock",
                location = "Los Angeles, CA",
                imageUrl = "/images/highvoltage.jpg",
                description = "This all-female classic rock/heavy metal band will get you up and moving.",
                tags = "Heavy Rock,Party,Loud",
            ),
            ArtistDto(
                id = 101,
                name = "Selfie and the SimChips",
                genre = "Pop",
                location = "Miami, FL",
                imageUrl = "/images/selfiesim.jpg",
                description = "A current pop group fronted by a dynamic female singer.",
                tags = "Pop Music,Modern,Dance",
            ),
            ArtistDto(
                id = 102,
                name = "Tony and Donna",
                genre = "Easy Listening",
                location = "New York, NY",
                imageUrl = "/images/donna.jpg",
                description = "A piano duo that has been entertaining audiences for over 12 years.",
                tags = "Piano,Duo,Adult Contemporary",
            ),
            ArtistDto(
                id = 105,
                name = "Carlos Dream",
                genre = "Rock",
                location = "New York, NY",
                imageUrl = "/images/CarlosDream.jpg",
                description = "Described by Entertainment Weekly as 'Barry White meets Al Green', Carlos will enchant you with his romantic, soulful sound.",
                tags = "Rock,Soul,Romance",
            ),
            ArtistDto(
                id = 104,
                name = "Joan Chandler",
                genre = "Mature",
                location = "New York, NY",
                imageUrl = "/images/chandler.jpg",
                description = "Joan's unique cultural and political viewpoint will have you laughing in your seat.",
                tags = "Comedian,Political,Mature",
            ),
            ArtistDto(
                id = 103,
                name = "The Magnificent Marco",
                genre = "Family",
                location = "Chicago, IL",
                imageUrl = "/images/marco.jpg",
                description = "Family-friendly stage and street magic performed with a witty flair.",
                tags = "Street Magic,Juggling,Unicycle",
            )
        )

    }

    override suspend fun getArtist(id: Int): ArtistDto? {
        return getArtists().firstOrNull { it.id == id }
    }

}