package com.pnc.jetpackcomposedemos.features.artists.presentation.composables

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.pnc.jetpackcomposedemos.features.artists.domain.Artist

@Composable
fun ArtistBookingCard(artist: Artist) {
    Log.d("ComposeDemo", "ArtistBookingCard: composing")
    // longer syntax, works with the mutableState wrapper around our value
//    val selectedState = remember {
//        mutableStateOf(false)
//    }
    // shorter syntax, works directly with our value
    var isSelected by remember {
        mutableStateOf(false)
    }

    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        ArtistInformation(artist = artist)
        BookingControls(
            isSelected = isSelected,
            onSelectionChanged = {
                isSelected = !isSelected
            }
        )
    }
}

@Composable
fun ArtistInformation(artist: Artist) {

    Log.d("ComposeDemo", "ArtistInformation: composing")

    Text(artist.name)
    Text(artist.genre)

}

@Composable
fun BookingControls(
    isSelected: Boolean,
    onSelectionChanged: () -> Unit
) {
    Log.d("ComposeDemo", "BookingControls: composing with $isSelected")

    Button(
        onClick = onSelectionChanged
    ) {
        Text(
            if (isSelected) {
                "Remove from event"
            } else {
                "Add to event"
            }
        )
    }
}




