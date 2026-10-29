package com.pnc.jetpackcomposedemos.features.auth.data.oidc

import java.util.Base64


class FakeAuthApi: AuthApi {

    var response: TokenResponse? = null
    var failure: Throwable? = null

    var exchangeCalls = 0
    var lastCode: String? = null
    var lastVerifier: String? = null
    var lastRedirectUri: String? = null

    var refreshCalls = 0
    var lastRefreshToken: String? = null
    var refreshFailure: Throwable? = null

    override suspend fun exchangeCode(
        grantType: String, clientId: String, code: String, redirectUri: String, codeVerifier: String
    ): TokenResponse {
        exchangeCalls++
        lastCode = code
        lastVerifier = codeVerifier
        lastRedirectUri = redirectUri
        failure?.let { throw it }
        return response!!
    }

    override suspend fun refresh(grantType: String, clientId: String, refreshToken: String): TokenResponse {
        refreshCalls++
        lastRefreshToken = refreshToken
        refreshFailure?.let { throw it }
        return response!!
    }

    companion object {
        /** A structurally valid (unsigned) JWT with the given claims. */
        fun idToken(sub: String, name: String?, nonce: String?): String {
            val claims = buildList {
                add("\"sub\":\"$sub\"")
                if (name != null) add("\"name\":\"$name\"")
                if (nonce != null) add("\"nonce\":\"$nonce\"")
            }.joinToString(",", "{", "}")
            val payload = Base64.getUrlEncoder().withoutPadding().encodeToString(claims.toByteArray())
            return "e30.$payload.signature"
        }
    }

}


