package com.pnc.jetpackcomposedemos.core

import com.pnc.jetpackcomposedemos.BuildConfig
import com.pnc.jetpackcomposedemos.features.auth.data.oidc.FakeAuthApi
import com.pnc.jetpackcomposedemos.features.auth.data.oidc.OidcClient
import com.pnc.jetpackcomposedemos.features.auth.data.oidc.TokenResponse
import com.pnc.jetpackcomposedemos.features.auth.data.storage.FakeTokenStore
import com.pnc.jetpackcomposedemos.features.auth.domain.AuthState
import com.pnc.jetpackcomposedemos.features.auth.domain.SessionManager
import com.pnc.jetpackcomposedemos.features.auth.domain.User
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import java.io.IOException

@OptIn(ExperimentalCoroutinesApi::class)
class TokenAuthenticatorTest {

    private val api = FakeAuthApi()
    private val scope = TestScope(UnconfinedTestDispatcher())
    private val session = SessionManager(FakeTokenStore(), scope)
    private val authenticator = TokenAuthenticator(session, OidcClient(api))

    private val user = User("11000", "Cara Cole", token = "old-access", refreshToken = "old-refresh")

    private val apiUrl = BuildConfig.AW_BASE_URL + "my/customers/11000/orders"

    private fun unauthorized(token: String, url: String = apiUrl, prior: Response? = null): Response {
        val request = Request.Builder().url(url).header("Authorization", "Bearer $token").build()
        return Response.Builder()
            .request(request).protocol(Protocol.HTTP_1_1).code(401).message("Unauthorized")
            .priorResponse(prior)
            .build()
    }

    private fun newTokens() = TokenResponse("new-access", null, "new-refresh", 1800, "Bearer")

    @Test
    fun `refreshes and retries the request with the new token`() {
        session.signIn(user)
        api.response = newTokens()

        val retry = authenticator.authenticate(null, unauthorized("old-access"))

        assertEquals("Bearer new-access", retry?.header("Authorization"))
        assertEquals("new-access", session.currentUser?.token)
        assertEquals("new-refresh", session.currentUser?.refreshToken)
    }

    @Test
    fun `retries with the current token if another request already refreshed`() {
        session.signIn(user.copy(token = "already-new"))

        val retry = authenticator.authenticate(null, unauthorized("old-access"))

        assertEquals("Bearer already-new", retry?.header("Authorization"))
        assertEquals(0, api.refreshCalls)
    }

    @Test
    fun `signs the user out when the refresh token is rejected`() {
        session.signIn(user)
        api.refreshFailure = retrofit2.HttpException(
            retrofit2.Response.error<Any>(
                400,
                """{"error":"invalid_grant"}""".toResponseBody("application/json".toMediaType())
            )
        )

        val retry = authenticator.authenticate(null, unauthorized("old-access"))

        assertNull(retry)
        assertEquals(AuthState.Unauthenticated, session.state.value)
    }

    @Test
    fun `stays signed in when the network is down`() {
        session.signIn(user)
        api.refreshFailure = IOException("offline")

        val retry = authenticator.authenticate(null, unauthorized("old-access"))

        assertNull(retry)
        assertEquals(user, session.currentUser)
    }

    @Test
    fun `gives up after one retry`() {
        session.signIn(user)
        api.response = newTokens()
        val first = unauthorized("old-access")

        val retry = authenticator.authenticate(null, unauthorized("new-access", prior = first))

        assertNull(retry)
        assertEquals(0, api.refreshCalls)
    }

    @Test
    fun `never refreshes for other hosts`() {
        session.signIn(user)

        val retry = authenticator.authenticate(
            null, unauthorized("old-access", url = "https://other.example.com/api/x")
        )

        assertNull(retry)
        assertEquals(0, api.refreshCalls)
    }

    @Test
    fun `does nothing when signed out`() {
        val retry = authenticator.authenticate(null, unauthorized("old-access"))

        assertNull(retry)
    }
}




