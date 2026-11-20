package com.pnc.jetpackcomposedemos.features.auth.data.oidc

data class PendingSignIn(
    val verifier: String,
    val state: String,
    val nonce: String
)


