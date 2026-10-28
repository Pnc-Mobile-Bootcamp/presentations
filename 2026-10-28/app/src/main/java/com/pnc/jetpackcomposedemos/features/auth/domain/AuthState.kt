package com.pnc.jetpackcomposedemos.features.auth.domain

sealed interface AuthState {
    data object Restoring: AuthState
    data class Authenticated(val user: User): AuthState
    data object Unauthenticated: AuthState
}


