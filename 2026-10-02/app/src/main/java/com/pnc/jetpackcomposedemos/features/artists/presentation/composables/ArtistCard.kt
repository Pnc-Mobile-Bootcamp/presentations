package com.pnc.jetpackcomposedemos.features.artists.presentation.composables

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pnc.jetpackcomposedemos.R
import com.pnc.jetpackcomposedemos.features.artists.domain.Artist
import com.pnc.jetpackcomposedemos.ui.theme.JetpackComposeDemosTheme

@Composable
fun ArtistCard(
    artist: Artist,
    onClick: () -> Unit
) {
    var isFavorite by remember {
        mutableStateOf(false)
    }

    val starScale by animateFloatAsState(
        targetValue = if (isFavorite) 1.5f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        )
    )

    Card(
        modifier = Modifier
            .fillMaxWidth(),
        onClick = onClick,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        ),
        shape = MaterialTheme.shapes.medium,
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            IconToggleButton(
                checked = isFavorite,
                onCheckedChange = {
                    isFavorite = it
                }
            ) {
                Icon(
                    painter = painterResource(
                        id = if (isFavorite) R.drawable.filled_star else R.drawable.outline_star
                    ),
                    contentDescription = if (isFavorite) "Favorite" else "Not favorite",
                    tint = if (isFavorite)
                        MaterialTheme.colorScheme.primary
                    else
                        MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.graphicsLayer {
                        scaleX = starScale
                        scaleY = starScale
                    }
                )
            }
            Text(
                text = artist.name,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                "Genre: ${artist.genre}"
            )
            Text("Location: ${artist.location}")
        }
    }

}


@Preview(showBackground = false)
@Composable
fun ArtistCardPreview() {
    val artist = Artist(
        id = 1,
        name = "Artist Name",
        genre = "Genre",
        location = "Location",
        imageUrl = "",
        description = "",
        tags = ""
    )

    JetpackComposeDemosTheme {
        ArtistCard(
            artist = artist,
            onClick = {}
        )
    }
}

