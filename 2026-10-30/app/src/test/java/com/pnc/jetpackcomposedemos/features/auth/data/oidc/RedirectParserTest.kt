package com.pnc.jetpackcomposedemos.features.auth.data.oidc

import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import org.junit.Test

class RedirectParserTest {

    @Test
    fun `reads code and state`() {

        val parsed = RedirectParser.parse("com.example.app:/oauth2redirect?code=abc&state=xyz")

        assertEquals("abc", parsed.code)
        assertEquals("xyz", parsed.state)
        assertNull(parsed.error)
    }

    @Test
    fun `reads an error response`() {

        val parsed = RedirectParser.parse(
            "com.example.app:/oauth2redirect?error=access_denied&error_description=No%20thanks"
        )

        assertEquals("access_denied", parsed.error)
        assertEquals("No thanks", parsed.errorDescription)
        assertNull(parsed.code)
    }

    @Test
    fun `a redirect with no query gives nothing`() {

        val parsed = RedirectParser.parse(
            "com.example.app:/oauth2redirect"
        )
        assertNull(parsed.code)
        assertNull(parsed.state)
    }

}


