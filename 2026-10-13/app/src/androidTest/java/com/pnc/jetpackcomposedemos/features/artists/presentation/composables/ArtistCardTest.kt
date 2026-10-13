package com.pnc.jetpackcomposedemos.features.artists.presentation.composables

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.printToLog
import com.pnc.jetpackcomposedemos.testutil.builders.anArtist
import com.pnc.jetpackcomposedemos.ui.theme.KazooTheme
import junit.framework.TestCase.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.experimental.theories.suppliers.TestedOn

class ArtistCardTest {

    @get:Rule
    val composeRule = createComposeRule()

    private val artist = anArtist(
        name = "High Voltage",
        genre = "Rock",
        location = "Los Angeles, CA",
        description = "Loud and proud"
    )

    private var clickCount = 0

    private fun setCard() {
        composeRule.setContent {
            KazooTheme {
                ArtistCard(
                    artist = artist,
                    onClick = { clickCount++ }
                )
            }
        }
    }

//    @Test
//    fun explore_printSemanticsTrees() {
//        setCard()
//        composeRule.onRoot().printToLog("ARTIST_CARD_MERGED")
//        composeRule.onRoot(useUnmergedTree = true).printToLog("ARTIST_CARD_UNMERGED")
//    }

    @Test
    fun givenArtist_whenShown_thenDisplaysNameGenreLocationAndDescription() {
        setCard()

        composeRule.onNodeWithText(artist.name, useUnmergedTree = true).assertIsDisplayed()
        composeRule.onNodeWithText("Genre: ${artist.genre}", useUnmergedTree = true).assertIsDisplayed()
        composeRule.onNodeWithText("Location: ${artist.location}", useUnmergedTree = true).assertIsDisplayed()
        composeRule.onNodeWithText(artist.description, useUnmergedTree = true).assertIsDisplayed()
    }

    @Test
    fun givenCard_whenTapped_thenOnClickIsCalledOnce() {

        setCard()

        composeRule.onNodeWithText(artist.name, useUnmergedTree = true).performClick()

        assertEquals(1, clickCount)
    }

}



