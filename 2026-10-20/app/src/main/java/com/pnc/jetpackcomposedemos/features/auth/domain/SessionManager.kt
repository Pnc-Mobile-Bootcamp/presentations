package com.pnc.jetpackcomposedemos.features.auth.domain

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

// this is the one place that knows who is signed in
@Singleton
class SessionManager @Inject constructor() {

    private val _state = MutableStateFlow<AuthState>(AuthState.Unauthenticated)

    val state: StateFlow<AuthState> = _state.asStateFlow()

    // utilities for non-composables (e.g. interceptors, repositories)
    val currentUser: User?
        get() = (_state.value as? AuthState.Authenticated)?.user

    fun signIn(user: User) {
        _state.value = AuthState.Authenticated(user)
    }

    fun signOut() {
        _state.value = AuthState.Unauthenticated
    }

}


