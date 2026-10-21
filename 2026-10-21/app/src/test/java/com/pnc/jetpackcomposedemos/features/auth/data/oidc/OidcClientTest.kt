package com.pnc.jetpackcomposedemos.features.auth.data.oidc

import com.pnc.jetpackcomposedemos.BuildConfig
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import org.junit.Test

class OidcClientTest {

    private val redirect = BuildConfig.OIDC_REDIRECT_URI

    @Test
    fun `beginSignIn builds a PKCE url`() {

        val url = OidcClient().beginSignIn()

        // challenge method is S256
        assertEquals("S256", url.queryParameter("code_challenge_method"))
        // client id in the url is the same as in our BuildConfig
        assertEquals(BuildConfig.OIDC_CLIENT_ID, url.queryParameter("client_id"))
        // code challenge in the url is 43 characters long
        assertEquals(43, url.queryParameter("code_challenge")?.length)

    }

    @Test
    fun `accepts a redirect carrying our state`() {
        val client = OidcClient()
        val state = client.beginSignIn().queryParameter("state")

        val mockServerResponse = "$redirect?code=abc&state=$state"

        val result = client.codeFrom(mockServerResponse)

        assertEquals("abc", result.getOrNull())
    }

    @Test
    fun `reject a redirect with a different state`() {
        val client = OidcClient()
        client.beginSignIn()

        val mockServerResponse = "$redirect?code=abc&state=somebody-else"

        val result = client.codeFrom(mockServerResponse)

        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull()!!.message!!.contains("state mismatch"))
    }

    @Test
    fun `reports an error from the server`() {
        val client = OidcClient()
        val state = client.beginSignIn().queryParameter("state")

        val mockServerResponse = "$redirect?error=access_denied&state=$state"

        val result = client.codeFrom(mockServerResponse)

        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull()!!.message!!.contains("access_denied"))
    }

    @Test
    fun `fails if no sign-in was started`() {
        val result = OidcClient().codeFrom("$redirect?code=abc&state=x")

        assertTrue(result.isFailure)
    }

}


