package com.pnc.jetpackcomposedemos.features.auth.domain

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: String,
    val username: String,
    val token: String,              // the access token
    val refreshToken: String? = null,
    val expiresAtMillis: Long = 0L  // when the access token expires
)


