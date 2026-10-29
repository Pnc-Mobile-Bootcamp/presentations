package com.pnc.jetpackcomposedemos.features.auth.data.oidc

import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrl

object AuthorizeUrl {

    fun build(
        authBaseUrl: String,
        clientId: String,
        redirectUri: String,
        scopes: String,
        state: String,
        nonce: String,
        codeChallenge: String,
        forceLogin: Boolean = true
    ): HttpUrl {
        return (authBaseUrl.trimEnd('/') + "/connect/authorize")
            .toHttpUrl()
            .newBuilder()
            .addQueryParameter("response_type", "code")
            .addQueryParameter("client_id", clientId)
            .addQueryParameter("redirect_uri", redirectUri)
            .addQueryParameter("scope", scopes)
            .addQueryParameter("state", state)
            .addQueryParameter("nonce", nonce)
            .addQueryParameter("code_challenge", codeChallenge)
            .addQueryParameter("code_challenge_method", "S256")
            .apply {
                if (forceLogin) addQueryParameter("prompt", "login")
            }
            .build()
    }

}


