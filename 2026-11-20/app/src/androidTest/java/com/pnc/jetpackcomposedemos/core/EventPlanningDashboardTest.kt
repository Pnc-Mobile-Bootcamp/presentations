package com.pnc.jetpackcomposedemos.core

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.pnc.jetpackcomposedemos.ui.theme.KazooTheme
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class EventPlanningDashboardTest {

    @get:Rule
    val composeRule = createComposeRule()

    // records which callback fired, in order
    private val calls = mutableListOf<String>()

    @Before
    fun setUp() {
        // render the composable being tested
        composeRule.setContent {
            KazooTheme() {
                EventPlanningDashboard(
                    onViewArtists = { calls.add("artists") },
                    onViewBoardMembers = { calls.add("board members") },
                    onViewOrders = { calls.add("orders") },
                    onViewToDos = { calls.add("todos") },
                    onSignOut = { calls.add("sign out") },
                    onSettings = { calls.add("settings") }
                )
            }
        }
    }


    @Test
    fun givenDashboard_whenShown_thenDisplaysTitle() {

        composeRule.onNode(hasTestTag("dashboard-title")).assertIsDisplayed()

    }

    @Test
    fun givenDashboard_whenShown_thenDisplaysAllTwoButtons() {
        listOf(
            "view-board-button",
            "sign-out-button"
        ).forEach { label ->
            composeRule.onNode(hasTestTag(label)).assertIsDisplayed()
        }
    }

//    @Test
//    fun givenDashboard_whenViewArtistsTapped_thenOnViewArtistsIsCalled() {
//
//        composeRule.onNode(hasTestTag("view-artists-button")).performClick()
//
//        assertEquals(listOf("artists"), calls)
//    }

    @Test
    fun givenDashboard_whenEachButtonIsTapped_thenOnlyItsOwnCallbackIsCalled() {
        val expectations = mapOf(
            "view-board-button" to "board members",
            "sign-out-button" to "sign out"
        )

        expectations.forEach { (label, expectedCall) ->
            calls.clear()
            composeRule.onNode(hasTestTag(label)).performClick()
            assertEquals("tapping $label", listOf(expectedCall), calls)
        }

    }

    @Test
    fun givenDashboard_whenNothingTapped_thenNoCallbacksAreCalled() {

        composeRule.waitForIdle()

        assertEquals(emptyList<String>(), calls)
    }

}


