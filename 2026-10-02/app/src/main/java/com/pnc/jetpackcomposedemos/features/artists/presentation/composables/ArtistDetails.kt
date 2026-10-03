package com.pnc.jetpackcomposedemos.features.artists.presentation.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.pnc.jetpackcomposedemos.features.artists.domain.Artist
import com.pnc.jetpackcomposedemos.ui.theme.JetpackComposeDemosTheme

@Composable
fun ArtistDetails(
    artist: Artist
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(Color.LightGray)
            .padding(16.dp)
    ) {
        Text(
            artist.name,
            style = MaterialTheme.typography.headlineMedium
        )
        Text(
            artist.genre,
            color = Color.Gray
        )
        Text(artist.location)
        AsyncImage(
            model = "https://kazoopromotions.com${artist.imageUrl}",
            contentDescription = "picture of ${artist.name}",
            modifier = Modifier.size(250.dp)
        )
        Text(artist.description)
    }
}

@Preview(showBackground = false)
@Composable
fun ArtistDetailsPreview() {
    val artist = Artist(
        id = 100,
        name = "High Voltage",
        genre = "Rock",
        location = "Los Angeles, CA",
        imageUrl = "/images/highvoltage.jpg",
        description = "This all-female classic rock/heavy metal band will get you up and moving.",
        tags = "Heavy Rock,Party,Loud",
    )

    JetpackComposeDemosTheme {
        ArtistDetails(
            artist = artist
        )
    }
}

