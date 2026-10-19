package com.pnc.jetpackcomposedemos.features.auth.data.oidc

import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import org.junit.Assert.assertNotEquals
import org.junit.Test

class PkceTest {

    @Test
    fun `challenge matches the example in RFC 7636 appendix B`() {
        val verifier = "dBjftJeZ4CVP-mB92K27uhbUJU1p1r_wW1gFWFOEjXk"

        val challenge = Pkce.codeChallenge(verifier)

        assertEquals("E9Melhoa2OwvFrEMTJguCHaoeK1t8URWbuGJSstw-cM", challenge)
    }

    @Test
    fun `verifier is 43 url-safe characters`() {
        val verifier = Pkce.newCodeVerifier()

        assertTrue(verifier.matches(Regex("^[A-Za-z0-9_-]{43}$")))
    }

    @Test
    fun `verifiers are different every time`() {
        assertNotEquals(Pkce.newCodeVerifier(), Pkce.newCodeVerifier())
    }

    @Test
    fun `the same verifier always gives the same challenge`() {
        val verifier = Pkce.newCodeVerifier()

        assertEquals(Pkce.codeChallenge(verifier), Pkce.codeChallenge(verifier))
    }

}