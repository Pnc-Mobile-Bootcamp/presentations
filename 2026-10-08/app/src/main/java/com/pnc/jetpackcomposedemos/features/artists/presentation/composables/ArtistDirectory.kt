package com.pnc.jetpackcomposedemos.features.artists.presentation.composables

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.Modifier
import androidx.compose.material.icons.filled.Filter
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.VerticalDivider
import androidx.compose.ui.Alignment
import androidx.compose.ui.tooling.preview.Preview
import com.pnc.jetpackcomposedemos.core.presentation.TextFilter
import com.pnc.jetpackcomposedemos.features.artists.domain.Artist
import com.pnc.jetpackcomposedemos.features.artists.presentation.state.ArtistDirectoryState

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ArtistDirectory(
    state: ArtistDirectoryState,
    useTwoPaneLayout: Boolean,
    onBack: () -> Unit = {},
    onArtistSelected: (Int) -> Unit = {},
    onGenreChange: (String) -> Unit = {},
    onLocationChange: (String) -> Unit = {},
    onShowFilterChange: (Boolean) -> Unit = {}
) {

    Scaffold(
        topBar = {
            Column {
                TopAppBar(
                    title = {
                        Text("Available Artists")
                    },
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Return to dashboard"
                            )
                        }
                    }
                )
                Text(
                    text = "Displaying ${state.displayedArtists.size} artists",
                    modifier = Modifier.padding(0.dp, 12.dp)
                )
            }
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                text = { Text("Show filter") },
                icon = {
                    Icon(Icons.Filled.Filter, contentDescription = "Show filter")
                },
                onClick = {
                    onShowFilterChange(true)
                }
            )
        }
    ) { innerPadding ->

        if (useTwoPaneLayout) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(modifier = Modifier.weight(1f)) {
                    ArtistList(
                        artists = state.displayedArtists,
                        onArtistSelected = onArtistSelected,
                        innerPadding = PaddingValues(0.dp)
                    )
                }
                VerticalDivider()
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text("Selected artist")
                    val selectedArtist = state.selectedArtist
                    AnimatedVisibility(
                        visible = selectedArtist != null,
                        enter = fadeIn() + expandVertically(expandFrom = Alignment.Top),
                        exit = fadeOut() + shrinkVertically(shrinkTowards = Alignment.Top)
                    ) {
                        if (selectedArtist != null) {
                            ArtistDetails(
                                artistId = selectedArtist.id
                            )
                        }
                    }

                }
            }

        } else {
            ArtistList(
                artists = state.displayedArtists,
                onArtistSelected = onArtistSelected,
                innerPadding = innerPadding
            )
        }


        if (state.showFilter) {
            ModalBottomSheet(
                onDismissRequest = {
                    onShowFilterChange(false)
                }
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Filter Artists",
                        style = MaterialTheme.typography.titleSmall
                    )
                    TextFilter(
                        label = "Genre",
                        filter = state.genreFilter,
                        onFilterChange = onGenreChange
                    )
                    TextFilter(
                        label = "Location",
                        filter = state.locationFilter,
                        onFilterChange = onLocationChange
                    )
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Button(
                            onClick = {
                                onGenreChange("")
                                onLocationChange("")
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                                contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                        ) {
                            Text("Clear")
                        }

                        Button(
                            onClick = {
                                onShowFilterChange(false)
                            }
                        ) {
                            Text("Close")
                        }
                    }


                }
            }
        }

    }

}


@Preview(
    name = "Compact View",
    widthDp = 390,
    heightDp = 844
)
@Composable
fun CompactArtistDirectoryPreview() {
    ArtistDirectory(
        state = ArtistDirectoryState(),
        useTwoPaneLayout = false,
    )
}

@Preview(
    name = "Expanded View",
    widthDp = 900,
    heightDp = 844
)
@Composable
fun ExpandedArtistDirectoryPreview() {
    ArtistDirectory(
        state = ArtistDirectoryState(),
        useTwoPaneLayout = true,
    )
}






