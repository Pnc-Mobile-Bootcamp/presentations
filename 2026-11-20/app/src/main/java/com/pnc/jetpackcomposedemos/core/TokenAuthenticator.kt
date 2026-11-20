package com.pnc.jetpackcomposedemos.core

import com.pnc.jetpackcomposedemos.BuildConfig
import com.pnc.jetpackcomposedemos.features.auth.data.oidc.OidcClient
import com.pnc.jetpackcomposedemos.features.auth.data.oidc.RefreshRejectedException
import com.pnc.jetpackcomposedemos.features.auth.domain.SessionManager
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenAuthenticator @Inject constructor(
    private val session: SessionManager,
    private val oidc: OidcClient
): Authenticator {

    private val apiHost = BuildConfig.AW_BASE_URL.toHttpUrl().host
    private val lock = Any()

    override fun authenticate(route: Route?, response: Response): Request? {

        if (response.request.url.host != apiHost) return null
        if (responseCount(response) >= 2) return null

        // Only want one refresh in-flight at a time.
        // if three requests fail together, the first should refresh
        // and the others will find the new token waiting.
        // The old refresh token can only be used once
        synchronized(lock) {
            val user = session.currentUser ?: return null
            val sentToken = response.request.header("Authorization")?.removePrefix("Bearer ")

            if (user.token != sentToken) {
                // someone else already refreshed while we waited for the lock
                return withToken(response.request, user.token)
            }

            // normally, web calls are async
            // OkHttp's Authenticator is already blocking
            val refreshed = runBlocking { oidc.refresh(user) }

            return refreshed.fold(
                onSuccess = { newUser ->
                    session.update(newUser)
                    withToken(response.request, newUser.token)
                },
                onFailure = { error ->
                    // refresh token rejected = the session is over
                    // a network error just fails this request
                    if (error is RefreshRejectedException) session.signOut()
                    null
                }
            )
        }
    }


    private fun withToken(request: Request, token: String) =
        request.newBuilder().header("Authorization", "Bearer $token").build()

    private fun responseCount(response: Response): Int {
        var count = 1
        var prior = response.priorResponse
        while (prior != null) {
            count++
            prior = prior.priorResponse
        }
        return count
    }


}



