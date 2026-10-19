package com.pnc.jetpackcomposedemos.features.auth.data.oidc

import java.security.MessageDigest
import java.security.SecureRandom
import java.util.Base64

object Pkce {

    /** 32 random bytes, base64url without padding = a 43-character secret. */
    fun newCodeVerifier(random: SecureRandom = SecureRandom()): String {
        val bytes = ByteArray(32)
        random.nextBytes(bytes)
        return base64Url(bytes)
    }

    /** code_challenge = BASE64URL( SHA-256( code_verifier ) )   (the "S256" method) */
    fun codeChallenge(codeVerifier: String): String {
        val hash = MessageDigest.getInstance("SHA-256")
            .digest(codeVerifier.toByteArray(Charsets.US_ASCII))
        return base64Url(hash)
    }

    /** Random value for `state` and `nonce`. Same recipe as the verifier. */
    fun newRandomValue(random: SecureRandom = SecureRandom()): String = newCodeVerifier(random)

    private fun base64Url(bytes: ByteArray): String =
        Base64.getUrlEncoder().withoutPadding().encodeToString(bytes)

}



