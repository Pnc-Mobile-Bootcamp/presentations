package com.pnc.jetpackcomposedemos.features.artists.domain

data class Artist (
    val id: Int,
    val name: String,
    val genre: String,
    val location: String,
    val imageUrl: String,
    val description: String,
    val tags: String,
) {

    // we would like a static function to generate some fake artist data
    // but Kotlin does not have "static" keyword to create class-level functions
    // Instead, we do it using a "companion object"
    companion object {
        fun getArtists(): List<Artist> {
            return listOf(
                Artist(
                    id = 100,
                    name = "High Voltage",
                    genre = "Rock",
                    location = "Los Angeles, CA",
                    imageUrl = "/images/highvoltage.jpg",
                    description = "This all-female classic rock/heavy metal band will get you up and moving.",
                    tags = "Heavy Rock,Party,Loud",
                ),
                Artist(
                    id = 101,
                    name = "Selfie and the SimChips",
                    genre = "Pop",
                    location = "Miami, FL",
                    imageUrl = "/images/selfiesim.jpg",
                    description = "A current pop group fronted by a dynamic female singer.",
                    tags = "Pop Music,Modern,Dance",
                ),
                Artist(
                    id = 102,
                    name = "Tony and Donna",
                    genre = "Easy Listening",
                    location = "New York, NY",
                    imageUrl = "/images/donna.jpg",
                    description = "A piano duo that has been entertaining audiences for over 12 years.",
                    tags = "Piano,Duo,Adult Contemporary",
                ),
                Artist(
                    id = 105,
                    name = "Carlos Dream",
                    genre = "Rock",
                    location = "New York, NY",
                    imageUrl = "/images/CarlosDream.jpg",
                    description = "Described by Entertainment Weekly as 'Barry White meets Al Green', Carlos will enchant you with his romantic, soulful sound.",
                    tags = "Rock,Soul,Romance",
                ),
                Artist(
                    id = 104,
                    name = "Joan Chandler",
                    genre = "Mature",
                    location = "New York, NY",
                    imageUrl = "/images/chandler.jpg",
                    description = "Joan's unique cultural and political viewpoint will have you laughing in your seat.",
                    tags = "Comedian,Political,Mature",
                ),
                Artist(
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


        fun generateArtists(num: Int): List<Artist> = List(num) { index ->
            Artist(
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


}