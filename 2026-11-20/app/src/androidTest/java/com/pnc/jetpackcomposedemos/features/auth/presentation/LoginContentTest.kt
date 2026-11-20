package com.pnc.jetpackcomposedemos.features.auth.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.hasProgressBarRangeInfo
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import com.pnc.jetpackcomposedemos.features.auth.domain.User
import com.pnc.jetpackcomposedemos.testutil.builders.aUser
import com.pnc.jetpackcomposedemos.ui.theme.KazooTheme
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class LoginContentTest {

    @get:Rule
    var composeRule = createComposeRule()

    private var uiState: LoginUiState by mutableStateOf(LoginUiState.Idle)

    private var loginAttempts = mutableListOf<Pair<String, String>>()
    private var loggedInUsers = mutableListOf<User>()

    // common UI items our tests will need to find:
    private val loadingIndicator = hasProgressBarRangeInfo(ProgressBarRangeInfo.Indeterminate)
    private val anyWelcome = hasText("Welcome", substring = true)
    private val anyError = hasText("Error:", substring = true)

    @Before
    fun setUp() {
        composeRule.setContent {
            KazooTheme {
                LoginContent(
                    uiState = uiState,
                    onLogin = { userId, passcode -> loginAttempts.add(Pair(userId, passcode)) },
                    onLoginSuccess = { user -> loggedInUsers.add(user) }
                )
            }
        }
    }

    // change state from the test thread safely, then let Compose recompose
    private fun showState(state: LoginUiState) {
        composeRule.runOnIdle { uiState = state }
    }


    // ---------- test the Idle state ------------------
    @Test
    fun givenIdle_whenShown_thenShowsBothFieldsAndTheLoginButton() {
        composeRule.onNodeWithText("User ID").assertIsDisplayed()
        composeRule.onNodeWithText("Passcode").assertIsDisplayed()
        composeRule.onNodeWithText("Login").assertIsDisplayed()
    }

    @Test
    fun givenIdle_whenShown_thenShowsNoSpinnerWelcomeOrError() {
        composeRule.onNode(loadingIndicator).assertDoesNotExist()
        composeRule.onNode(anyWelcome).assertDoesNotExist()
        composeRule.onNode(anyError).assertDoesNotExist()
    }


    // -------------- Loading state ----------------------
    @Test
    fun givenLoading_whenShown_thenShowsASpinnerAndNothingElse() {
        showState(LoginUiState.Loading)

        composeRule.onNode(loadingIndicator).assertIsDisplayed()
        composeRule.onNode(anyWelcome).assertDoesNotExist()
        composeRule.onNode(anyError).assertDoesNotExist()

    }


    // ------------- Success state -----------------------
    @Test
    fun givenSuccess_whenShown_thenGreetsTheUserByUsername() {
        showState(LoginUiState.Success(aUser(username = "Steve")))

        composeRule.onNodeWithText("Welcome, Steve").assertIsDisplayed()

    }

    @Test
    fun givenSuccess_whenShown_thenOnLoginSuccessReceivesTheUserOnce() {
        val user = aUser(username = "Gloria")

        showState(LoginUiState.Success(user))
        composeRule.waitForIdle()

        assertEquals(listOf(user), loggedInUsers)
    }

    @Test
    fun givenIdleLoadingAndError_whenShown_thenOnLoginSuccessIsNeverCalled() {

        showState(LoginUiState.Loading)
        showState(LoginUiState.Error("Invalid credentials"))
        showState(LoginUiState.Idle)

        composeRule.waitForIdle()

        assertEquals(emptyList<User>(), loggedInUsers)

    }

    @Test
    fun givenTypedCredentials_whenLoginTapped_thenOnLoginReceivesThem() {
        composeRule.onNodeWithText("User ID").performTextInput("hello")
        composeRule.onNodeWithText("Passcode").performTextInput("world")

        composeRule.onNodeWithText("Login").performClick()

        assertEquals(listOf(Pair("hello", "world")), loginAttempts)

    }

    @Test
    fun givenLoading_whenShown_thenLoginButtonIsDisabled() {
        showState(LoginUiState.Loading)

        composeRule.onNodeWithText("Login").assertIsNotEnabled()
    }


}








