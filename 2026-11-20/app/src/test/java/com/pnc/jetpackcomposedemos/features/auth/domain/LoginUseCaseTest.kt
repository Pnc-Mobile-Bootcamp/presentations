package com.pnc.jetpackcomposedemos.features.auth.domain

import com.pnc.jetpackcomposedemos.testutil.builders.aUser
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test


class LoginUseCaseTest {

    @Test
    fun `given blank user id, when logging in, then fails without calling the repository`() = runTest {
        val repo = RecordingAuthRepository()
        val useCase = LoginUseCase(repo)

        val result = useCase("", "secret123")

        assertTrue(result.isFailure)
        assertEquals("Credentials cannot be empty", result.exceptionOrNull()?.message)
        assertEquals(0, repo.loginCalls)
    }

    @Test
    fun `given valid credentials, when logging in, then returns the repository's user`() = runTest {
        val expected = aUser(username = "hello")
        val repo = RecordingAuthRepository(result = Result.success(expected))
        val useCase = LoginUseCase(repo)

        val result = useCase("drew", "somepassword")

        assertEquals(expected, result.getOrNull())
        assertEquals("drew", repo.lastUserId)
        assertEquals("somepassword", repo.lastPasscode)
    }



    private fun assertEmptyCredentialsFailure(result: Result<User>) {
        Assert.assertTrue(result.isFailure)
        Assert.assertTrue(result.exceptionOrNull() is IllegalArgumentException)
        Assert.assertEquals("Credentials cannot be empty", result.exceptionOrNull()?.message)
    }


    @Test
    fun `given whitespace-only user id, when logging in, then fails with empty credentials message`() = runTest {
        val repo = RecordingAuthRepository()

        val result = LoginUseCase(repo)("   ", "secret123")

        assertEmptyCredentialsFailure(result)
    }

    @Test
    fun `given empty passcode, when logging in, then fails with empty credentials message`() = runTest {
        val repo = RecordingAuthRepository()

        val result = LoginUseCase(repo)("hello", "")

        assertEmptyCredentialsFailure(result)
    }

    @Test
    fun `given whitespace-only passcode of valid length, when logging in, then fails as empty rather than too short`() = runTest {
        val repo = RecordingAuthRepository()

        // 6 spaces: long enough to pass the length rule, but blank
        val result = LoginUseCase(repo)("hello", "      ")

        assertEmptyCredentialsFailure(result)
    }

    @Test
    fun `given blank user id and short passcode, when logging in, then reports empty credentials first`() = runTest {
        val repo = RecordingAuthRepository()

        val result = LoginUseCase(repo)("", "abc")

        // Both rules are violated; the blank check wins because it runs first
        assertEmptyCredentialsFailure(result)
    }

    @Test
    fun `given blank credentials, when logging in, then never calls the repository`() = runTest {
        val repo = RecordingAuthRepository()

        LoginUseCase(repo)("", "")

        Assert.assertEquals(0, repo.loginCalls)
    }


    @Test
    fun `given passcode of 4 characters, when logging in, then fails with minimum length message`() = runTest {
        val repo = RecordingAuthRepository()

        val result = LoginUseCase(repo)("hello", "abcd")

        Assert.assertTrue(result.isFailure)
        Assert.assertTrue(result.exceptionOrNull() is IllegalArgumentException)
        Assert.assertEquals(
            "Passcode must be at least 5 characters",
            result.exceptionOrNull()?.message
        )
        Assert.assertEquals(0, repo.loginCalls)
    }

    @Test
    fun `given passcode of exactly 5 characters, when logging in, then passes validation and calls the repository`() = runTest {
        val repo = RecordingAuthRepository()

        val result = LoginUseCase(repo)("hello", "abcde")

        Assert.assertTrue(result.isSuccess)
        Assert.assertEquals(1, repo.loginCalls)
    }


    @Test
    fun `given valid credentials, when logging in, then passes them to the repository unchanged`() = runTest {
        val repo = RecordingAuthRepository()

        LoginUseCase(repo)("hello", "world1")

        Assert.assertEquals(1, repo.loginCalls)
        Assert.assertEquals("hello", repo.lastUserId)
        Assert.assertEquals("world1", repo.lastPasscode)
    }

    @Test
    fun `given repository rejects the credentials, when logging in, then propagates the failure`() = runTest {
        val failure = IllegalArgumentException("Invalid credentials")
        val repo = RecordingAuthRepository(result = Result.failure(failure))

        val result = LoginUseCase(repo)("hello", "wrong-pass")

        Assert.assertTrue(result.isFailure)
        Assert.assertEquals(failure, result.exceptionOrNull())
    }

}