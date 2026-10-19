package com.pnc.jetpackcomposedemos.features.auth.data.oidc

import com.pnc.jetpackcomposedemos.BuildConfig
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

class OidcClientTest {

    private val redirect = BuildConfig.OIDC_REDIRECT_URI
    private val api = FakeAuthApi()
    private val client = OidcClient(api)

    private fun tokens(idToken: String?) = TokenResponse(
        accessToken = "access-123", idToken = idToken, refreshToken = "refresh-456",
        expiresIn = 1800, tokenType = "Bearer"
    )

    /** Starts a sign-in and returns the redirect the browser would send back. */
    private fun startAndRedirect(code: String = "the-code"): Pair<String, String> {
        val url = client.beginSignIn()
        val state = url.queryParameter("state")!!
        val nonce = url.queryParameter("nonce")!!
        return "$redirect?code=$code&state=$state" to nonce
    }

    // ---- the browser half (Demo 3) ----

    @Test
    fun `beginSignIn builds a PKCE url`() {
        val url = client.beginSignIn()

        assertEquals("S256", url.queryParameter("code_challenge_method"))
        assertEquals(BuildConfig.OIDC_CLIENT_ID, url.queryParameter("client_id"))
        assertEquals(43, url.queryParameter("code_challenge")?.length)
    }

    @Test
    fun `codeFrom rejects a redirect with a different state`() {
        client.beginSignIn()

        val result = client.codeFrom("$redirect?code=abc&state=somebody-elses")

        assertTrue(result.exceptionOrNull()!!.message!!.contains("state mismatch"))
    }

    @Test
    fun `codeFrom reports an error from the server`() {
        val state = client.beginSignIn().queryParameter("state")

        val result = client.codeFrom("$redirect?error=access_denied&state=$state")

        assertTrue(result.exceptionOrNull()!!.message!!.contains("access_denied"))
    }

    // ---- the token half (Demo 4) ----

    @Test
    fun `completeSignIn returns a user built from the tokens`() = runTest {
        val (redirectUri, nonce) = startAndRedirect()
        api.response = tokens(FakeAuthApi.idToken("11000", "Cara Cole", nonce))

        val user = client.completeSignIn(redirectUri).getOrThrow()

        assertEquals("11000", user.id)
        assertEquals("Cara Cole", user.username)
        assertEquals("access-123", user.token)
        assertEquals("refresh-456", user.refreshToken)
        assertTrue(user.expiresAtMillis > System.currentTimeMillis())
    }

    @Test
    fun `the verifier sent to the server hashes to the challenge sent to the browser`() = runTest {
        val url = client.beginSignIn()
        val state = url.queryParameter("state")
        val nonce = url.queryParameter("nonce")!!
        api.response = tokens(FakeAuthApi.idToken("1", "A", nonce))

        client.completeSignIn("$redirect?code=the-code&state=$state").getOrThrow()

        // This is the whole point of PKCE.
        assertEquals(url.queryParameter("code_challenge"), Pkce.codeChallenge(api.lastVerifier!!))
        assertEquals("the-code", api.lastCode)
        assertEquals(redirect, api.lastRedirectUri)
    }

    @Test
    fun `a state mismatch never reaches the server`() = runTest {
        client.beginSignIn()

        val result = client.completeSignIn("$redirect?code=abc&state=forged")

        assertTrue(result.isFailure)
        assertEquals(0, api.exchangeCalls)
    }

    @Test
    fun `an ID token with the wrong nonce is rejected`() = runTest {
        val (redirectUri, _) = startAndRedirect()
        api.response = tokens(FakeAuthApi.idToken("1", "A", "somebody-elses-nonce"))

        val result = client.completeSignIn(redirectUri)

        assertTrue(result.exceptionOrNull()!!.message!!.contains("nonce"))
    }

    @Test
    fun `a missing ID token is rejected`() = runTest {
        val (redirectUri, _) = startAndRedirect()
        api.response = tokens(idToken = null)

        assertTrue(client.completeSignIn(redirectUri).isFailure)
    }

    @Test
    fun `a missing name falls back to the customer id`() = runTest {
        val (redirectUri, nonce) = startAndRedirect()
        api.response = tokens(FakeAuthApi.idToken("11000", name = null, nonce = nonce))

        assertEquals("Customer 11000", client.completeSignIn(redirectUri).getOrThrow().username)
    }

    @Test
    fun `the server's error description is shown`() = runTest {
        val (redirectUri, _) = startAndRedirect()
        val body = """{"error":"invalid_grant","error_description":"The code is invalid."}"""
        api.failure = HttpException(
            Response.error<Any>(400, body.toResponseBody("application/json".toMediaType()))
        )

        val result = client.completeSignIn(redirectUri)

        assertEquals("The code is invalid.", result.exceptionOrNull()!!.message)
    }

    @Test
    fun `no network gives a friendly message`() = runTest {
        val (redirectUri, _) = startAndRedirect()
        api.failure = IOException("boom")

        val result = client.completeSignIn(redirectUri)

        assertTrue(result.exceptionOrNull()!!.message!!.contains("Could not reach the server"))
    }

    @Test
    fun `a sign-in can be completed only once`() = runTest {
        val (redirectUri, nonce) = startAndRedirect()
        api.response = tokens(FakeAuthApi.idToken("1", "A", nonce))

        assertTrue(client.completeSignIn(redirectUri).isSuccess)
        assertTrue(client.completeSignIn(redirectUri).isFailure)
        assertEquals(1, api.exchangeCalls)
    }

    @Test
    fun `completing without starting fails`() = runTest {
        val result = client.completeSignIn("$redirect?code=abc&state=x")

        assertTrue(result.isFailure)
        assertNull(api.lastCode)
    }
}

