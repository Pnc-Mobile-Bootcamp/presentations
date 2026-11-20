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
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DockedSearchBar
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.VerticalDivider
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.pnc.jetpackcomposedemos.R
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
    onRefresh: () -> Unit = {},
    onDismissError: () -> Unit = {},
    onSearchQueryChange: (String) -> Unit = {},
    onGenreChange: (String) -> Unit = {},
    onLocationChange: (String) -> Unit = {},
    onShowFilterChange: (Boolean) -> Unit = {}
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val retryLabel = stringResource(R.string.artists_retry)

    // We're going to add a search bar (text input) to the top of the screen
    // Being the first thing that can take focus, Android would focus it
    // when the screen load. We don't want that. We want the user to
    // tap on the text input when they want to search.
    val focusManager = LocalFocusManager.current
    LaunchedEffect(Unit) {
        // pause (delay) until the next rendering frame executes
        withFrameNanos {}
        focusManager.clearFocus(force = true)
    }

    // A failed refresh should keep the cached artists on screen and
    // tell the user why, with a way to try again
    LaunchedEffect(state.errorMessage) {
        val message = state.errorMessage ?: return@LaunchedEffect
        val result = snackbarHostState.showSnackbar(
            message = message,
            actionLabel = retryLabel,
            duration = SnackbarDuration.Long
        )
        onDismissError()
        if (result == SnackbarResult.ActionPerformed) {
            onRefresh()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            Column {
                TopAppBar(
                    title = {
                        Text(stringResource(R.string.artists_title))
                    },
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = stringResource(R.string.artists_back)
                            )
                        }
                    }
                )

                DockedSearchBar(
                    inputField = {
                        SearchBarDefaults.InputField(
                            query = state.searchQuery,
                            onQueryChange = onSearchQueryChange,
                            onSearch = {},
                            expanded = false,
                            onExpandedChange = {},
                            placeholder = { Text(stringResource(R.string.artists_search_hint)) },
                            leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null) }
                        )
                    },
                    expanded = false,
                    onExpandedChange = {},
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    content = {}
                )

                Text(
                    text = pluralStringResource(
                        R.plurals.artists_displaying,
                        state.displayedArtists.size,
                        state.displayedArtists.size
                    ),
                    modifier = Modifier.padding(0.dp, 12.dp)
                )
            }
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                text = { Text(stringResource(R.string.artists_show_filter)) },
                icon = {
                    Icon(Icons.Filled.Filter, contentDescription = stringResource(R.string.artists_show_filter))
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
                PullToRefreshBox(
                    isRefreshing = state.isRefreshing,
                    onRefresh = onRefresh,
                    modifier = Modifier.weight(1f)
                ) {
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
                    Text(stringResource(R.string.artists_selected))
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
            PullToRefreshBox(
                isRefreshing = state.isRefreshing,
                onRefresh = onRefresh,
                modifier = Modifier.fillMaxSize().padding(innerPadding)
            ) {
                ArtistList(
                    artists = state.displayedArtists,
                    onArtistSelected = onArtistSelected,
                    innerPadding = PaddingValues(0.dp)
                )
            }
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
                        text = stringResource(R.string.artists_filter_title),
                        style = MaterialTheme.typography.titleSmall
                    )
                    TextFilter(
                        label = stringResource(R.string.artists_genre_label),
                        filter = state.genreFilter,
                        onFilterChange = onGenreChange
                    )
                    TextFilter(
                        label = stringResource(R.string.artists_location_label),
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
                            Text(stringResource(R.string.common_clear))
                        }

                        Button(
                            onClick = {
                                onShowFilterChange(false)
                            }
                        ) {
                            Text(stringResource(R.string.common_close))
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






