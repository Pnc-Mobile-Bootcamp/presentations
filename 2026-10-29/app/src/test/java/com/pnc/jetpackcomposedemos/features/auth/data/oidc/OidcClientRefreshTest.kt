package com.pnc.jetpackcomposedemos.features.auth.data.oidc

import com.pnc.jetpackcomposedemos.features.auth.domain.User
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException


class OidcClientRefreshTest {

    private val api = FakeAuthApi()
    private val client = OidcClient(api)
    private val user =
        User("11000", "Cara Cole", token = "old-access", refreshToken = "old-refresh")

    private fun rejection(code: Int) = HttpException(
        Response.error<Any>(
            code,
            """{"error":"invalid_grant"}""".toResponseBody("application/json".toMediaType())
        )
    )

    @Test
    fun `refresh keeps the newest tokens`() = runTest {
        api.response = TokenResponse("new-access", null, "new-refresh", 1800, "Bearer")

        val refreshed = client.refresh(user).getOrThrow()

        assertEquals("new-access", refreshed.token)
        assertEquals("new-refresh", refreshed.refreshToken)
        assertEquals("old-refresh", api.lastRefreshToken)
        assertEquals("11000", refreshed.id)
    }

    @Test
    fun `keeps the old refresh token if the server does not send a new one`() = runTest {
        api.response = TokenResponse("new-access", null, null, 1800, "Bearer")

        assertEquals("old-refresh", client.refresh(user).getOrThrow().refreshToken)
    }

    @Test
    fun `a rejected refresh token ends the session`() = runTest {
        api.refreshFailure = rejection(400)

        assertTrue(client.refresh(user).exceptionOrNull() is RefreshRejectedException)
    }

    @Test
    fun `a server error does not end the session`() = runTest {
        api.refreshFailure = rejection(503)

        val error = client.refresh(user).exceptionOrNull()

        assertTrue(error !is RefreshRejectedException)
    }

    @Test
    fun `no network does not end the session`() = runTest {
        api.refreshFailure = IOException("offline")

        assertTrue(client.refresh(user).exceptionOrNull() !is RefreshRejectedException)
    }

    @Test
    fun `without a refresh token the session is over`() = runTest {
        val result = client.refresh(user.copy(refreshToken = null))

        assertTrue(result.exceptionOrNull() is RefreshRejectedException)
        assertEquals(0, api.refreshCalls)
    }
}


