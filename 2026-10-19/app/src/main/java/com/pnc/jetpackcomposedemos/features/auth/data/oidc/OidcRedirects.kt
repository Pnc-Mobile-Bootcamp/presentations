package com.pnc.jetpackcomposedemos.features.auth.data.oidc

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OidcRedirects @Inject constructor() {

    // CONFLATED: keep only the latest value, and hold it until someone is collecting.
    private val channel = Channel<String>(Channel.CONFLATED)

    val redirects: Flow<String> = channel.receiveAsFlow()

    fun deliver(redirectUri: String) {
        channel.trySend(redirectUri)
    }
}


