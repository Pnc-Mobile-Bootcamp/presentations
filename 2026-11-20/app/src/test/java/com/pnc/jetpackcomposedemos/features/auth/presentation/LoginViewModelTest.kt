package com.pnc.jetpackcomposedemos.features.auth.presentation

import com.pnc.jetpackcomposedemos.features.auth.domain.LoginUseCase
import com.pnc.jetpackcomposedemos.features.auth.domain.RecordingAuthRepository
import com.pnc.jetpackcomposedemos.testutil.MainDispatcherRule
import com.pnc.jetpackcomposedemos.testutil.builders.aUser
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()
    // NOTE: when using this Rule approach (instead of @Before and @After)
    // do NOT create the ViewModel in a class constructor, or
    // property initializer - those execute before the test starting
    // method in our MainDispatcherRule
    // (create the ViewModel inside the test, or in its own @Before fun)

    @Test
    fun `given valid credentials, when logging in, then state is Success`() = runTest {
        val user = aUser()
        val repo = RecordingAuthRepository(result = Result.success(user))
        val viewModel = LoginViewModel(LoginUseCase(repo))

        viewModel.login("hello", "world")
        // assertions on the initial state
        advanceUntilIdle()  // wait until the coroutine launched by login() is finished

        assertEquals(LoginUiState.Success(user), viewModel.uiState.value)
    }

}