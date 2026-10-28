package com.pnc.jetpackcomposedemos.features.auth.presentation

import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasProgressBarRangeInfo
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import com.pnc.jetpackcomposedemos.features.auth.domain.AuthRepository
import com.pnc.jetpackcomposedemos.features.auth.domain.LoginUseCase
import com.pnc.jetpackcomposedemos.features.auth.domain.User
import com.pnc.jetpackcomposedemos.testutil.builders.aUser
import com.pnc.jetpackcomposedemos.ui.theme.KazooTheme
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.delay
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalTestApi::class)
class LoginScreenSynchronizationTest {

    @get:Rule
    val composeRule = createComposeRule()

    private class SlowAuthRepository(
        private val delayMillis: Long,
        private val result: Result<User>
    ): AuthRepository {

        override suspend fun login(userId: String, passcode: String): Result<User> {
            delay(delayMillis)
            return result
        }

    }

    private class GatedAuthRepository: AuthRepository {

        val gate = CompletableDeferred<Result<User>>()

        override suspend fun login(userId: String, passcode: String): Result<User> = gate.await()

    }

    private val loggedInUsers = mutableListOf<User>()

    private fun setLoginScreen(repository: AuthRepository) {

        val viewModel = LoginViewModel(LoginUseCase(repository))
        composeRule.setContent {
            KazooTheme {
                LoginScreen(
                    viewModel = viewModel,
                    onLoginSuccess = { loggedInUsers.add(it) }
                )
            }
        }
    }

    private val loadingIndicator = hasProgressBarRangeInfo(ProgressBarRangeInfo.Indeterminate)

    private fun enterCredentialsAndTapLogin() {
        composeRule.onNodeWithText("User ID").performTextInput("hello")
        composeRule.onNodeWithText("Passcode").performTextInput("world")
        composeRule.onNodeWithText("Login").performClick()
    }

    // ---------- 1. the mistake: asserting without waiting ----------

    // Remove @Ignore live. It fails: Compose waits for recomposition and animations, but it
    // cannot know that a coroutine in the view model is still sleeping for 500 ms.
//    @Ignore("Demonstration: fails because the login has not finished yet")
//    @Test
//    fun givenSlowLogin_whenAssertedImmediately_thenFails() {
//        setLoginScreen(SlowAuthRepository(500, Result.success(aUser())))
//
//        enterCredentialsAndTapLogin()
//
//        composeRule.onNodeWithText("Welcome, hello").assertIsDisplayed()   // not there yet
//    }

    // ---------- 2. the fix: wait for the condition ----------

    @Test
    fun givenSlowLogin_whenLoginTapped_thenWelcomeAppearsOnceLoginCompletes() {
        setLoginScreen(SlowAuthRepository(500, Result.success(aUser(username = "hello"))))

        enterCredentialsAndTapLogin()

        // The default timeout is only 1 second. State the timeout you actually need.
        composeRule.waitUntilExactlyOneExists(hasText("Welcome, hello"), timeoutMillis = 5_000)
        composeRule.onNodeWithText("Welcome, hello").assertIsDisplayed()
    }

    @Test
    fun givenSlowLogin_whenLoginSucceeds_thenOnLoginSuccessReceivesTheUser() {
        val user = aUser(username = "hello")
        setLoginScreen(SlowAuthRepository(300, Result.success(user)))

        enterCredentialsAndTapLogin()

        composeRule.waitUntil(timeoutMillis = 5_000) { loggedInUsers.isNotEmpty() }
        assertEquals(listOf(user), loggedInUsers)
    }

    // ---------- 3. a deterministic version: a gate instead of a timer ----------

    @Test
    fun givenGatedLogin_whenLoginTapped_thenLoadingShowsUntilTheGateOpens() {
        val repository = GatedAuthRepository()
        setLoginScreen(repository)

        enterCredentialsAndTapLogin()

        // The repository is parked on the gate, so Loading is guaranteed to be on screen.
        composeRule.onNode(loadingIndicator).assertIsDisplayed()
        composeRule.onNodeWithText("Welcome, hello").assertDoesNotExist()

        repository.gate.complete(Result.success(aUser(username = "hello")))

        composeRule.waitUntilExactlyOneExists(hasText("Welcome, hello"), timeoutMillis = 5_000)
        composeRule.onNode(loadingIndicator).assertDoesNotExist()
    }

    @Test
    fun givenGatedLogin_whenRepositoryFails_thenErrorMessageAppearsAndLoadingStops() {
        val repository = GatedAuthRepository()
        setLoginScreen(repository)

        enterCredentialsAndTapLogin()
        composeRule.onNode(loadingIndicator).assertIsDisplayed()

        repository.gate.complete(Result.failure(IllegalArgumentException("Invalid credentials")))

        composeRule.waitUntilExactlyOneExists(
            hasText("Error: Invalid credentials"),
            timeoutMillis = 5_000
        )
        composeRule.onNode(loadingIndicator).assertDoesNotExist()
        assertEquals(emptyList<User>(), loggedInUsers)
    }

    // ---------- 4. waiting for something to disappear ----------

    @Test
    fun givenGatedLogin_whenGateOpens_thenLoadingIndicatorGoesAway() {
        val repository = GatedAuthRepository()
        setLoginScreen(repository)
        enterCredentialsAndTapLogin()
        composeRule.onNode(loadingIndicator).assertIsDisplayed()

        repository.gate.complete(Result.success(aUser()))

        composeRule.waitUntilDoesNotExist(loadingIndicator, timeoutMillis = 5_000)
    }


}



