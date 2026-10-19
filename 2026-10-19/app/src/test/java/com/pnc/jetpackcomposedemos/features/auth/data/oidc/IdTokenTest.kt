package com.pnc.jetpackcomposedemos.features.auth.data.oidc

import junit.framework.TestCase.assertEquals
import org.junit.Test

class IdTokenTest {

    @Test
    fun `reads sub name and nonce`() {
        val claims = IdToken.parse(FakeAuthApi.idToken("42", "Ann Lee", "n1"))
        assertEquals("42", claims.subject)
        assertEquals("Ann Lee", claims.name)
        assertEquals("n1", claims.nonce)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `rejects something that is not a JWT`() {
        IdToken.parse("nope")
    }

}


