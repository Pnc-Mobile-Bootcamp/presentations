package com.pnc.jetpackcomposedemos.features.artists.presentation.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import com.pnc.jetpackcomposedemos.core.LoadableState
import com.pnc.jetpackcomposedemos.features.artists.domain.Artist
import com.pnc.jetpackcomposedemos.features.artists.presentation.viewmodels.ArtistDetailsViewModel
import com.pnc.jetpackcomposedemos.ui.theme.JetpackComposeDemosTheme

@Composable
fun ArtistDetails(
    artistId: Int,
    viewModel: ArtistDetailsViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(artistId) {
        viewModel.loadArtist(artistId)
    }

    when (state) {
        is LoadableState.Loading -> {
            CircularProgressIndicator()
        }
        is LoadableState.Empty -> {
            Text("Artist not found")
        }
        is LoadableState.Success -> {
            val artist = (state as LoadableState.Success<Artist>).data

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
        is LoadableState.Error -> {
            val message = (state as LoadableState.Error).message
            Text(
                "Error: $message",
                color = MaterialTheme.colorScheme.error
                )
        }
    }


}

//@Preview(showBackground = false)
//@Composable
//fun ArtistDetailsPreview() {
//    val artist = Artist(
//        id = 100,
//        name = "High Voltage",
//        genre = "Rock",
//        location = "Los Angeles, CA",
//        imageUrl = "/images/highvoltage.jpg",
//        description = "This all-female classic rock/heavy metal band will get you up and moving.",
//        tags = "Heavy Rock,Party,Loud",
//    )
//
//    JetpackComposeDemosTheme {
//        ArtistDetails(
//            artist = artist
//        )
//    }
//}

