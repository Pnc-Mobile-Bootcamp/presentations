package com.pnc.jetpackcomposedemos.features.auth.data.oidc

import com.pnc.jetpackcomposedemos.BuildConfig
import okhttp3.HttpUrl
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OidcClient @Inject constructor() {

    private var pending: PendingSignIn? = null

    // create fresh secrets, remember them, and return the URL
    // that should be opened in the browser
    fun beginSignIn(): HttpUrl {

        val request = PendingSignIn(
            verifier = Pkce.newCodeVerifier(),
            state = Pkce.newRandomValue(),
            nonce = Pkce.newRandomValue()
        )

        pending = request

        return AuthorizeUrl.build(
            authBaseUrl = BuildConfig.AUTH_BASE_URL,
            clientId = BuildConfig.OIDC_CLIENT_ID,
            redirectUri = BuildConfig.OIDC_REDIRECT_URI,
            scopes = BuildConfig.OIDC_SCOPES,
            state = request.state,
            nonce = request.nonce,
            codeChallenge = Pkce.codeChallenge(request.verifier)
        )

    }

    // check whether the browser's answer corresponds to OUR request
    // and return the auth code
    fun codeFrom(redirect: String): Result<String> {
        val request = pending ?:
            return Result.failure(IllegalStateException("No sign-in is in progress."))

        val parsed = RedirectParser.parse(redirect)

        if (parsed.error != null) {
            val detail = parsed.errorDescription?.let { ": $it" } ?: ""
            return Result.failure(IllegalStateException("${parsed.error}$detail"))
        }
        if (parsed.state != request.state) {
            return Result.failure(
                IllegalStateException("The response did not match the sign-in request (state mismatch).")
            )
        }

        val code = parsed.code ?: return Result.failure(IllegalStateException("The response contained no code."))

        return Result.success(code)
    }

}


