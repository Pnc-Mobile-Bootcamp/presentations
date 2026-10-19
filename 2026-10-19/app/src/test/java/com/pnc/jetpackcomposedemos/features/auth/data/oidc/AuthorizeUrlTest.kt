package com.pnc.jetpackcomposedemos.features.auth.data.oidc

import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import org.junit.Test

class AuthorizeUrlTest {

    private fun build(forceLogin: Boolean = true) = AuthorizeUrl.build(
        authBaseUrl = "https://api.example.com/",
        clientId = "my-client",
        redirectUri = "com.example.app:/oauth2redirect",
        scopes = "openid profile",
        state = "the-state",
        nonce = "the-nonce",
        codeChallenge = "the-challenge",
        forceLogin = forceLogin
    )

    @Test
    fun `points at the authorize endpoint`() {
        val url = build()

        assertEquals("api.example.com", url.host)
        assertEquals("/connect/authorize", url.encodedPath)
    }

    @Test
    fun `carries every parameter the server needs`() {
        val url = build()

        assertEquals("code", url.queryParameter("response_type"))
        assertEquals("my-client", url.queryParameter("client_id"))
        assertEquals("com.example.app:/oauth2redirect", url.queryParameter("redirect_uri"))
        assertEquals("openid profile", url.queryParameter("scope"))
        assertEquals("the-state", url.queryParameter("state"))
        assertEquals("the-nonce", url.queryParameter("nonce"))
        assertEquals("the-challenge", url.queryParameter("code_challenge"))
        assertEquals("S256", url.queryParameter("code_challenge_method"))
    }

    @Test
    fun `forces a fresh login by default`() {
        assertEquals("login", build().queryParameter("prompt"))
    }

    @Test
    fun `can leave out prompt`() {
        assertNull(build(forceLogin = false).queryParameter("prompt"))
    }


}


