package com.pnc.jetpackcomposedemos.features.auth.domain

import com.pnc.jetpackcomposedemos.core.ApplicationScope
import com.pnc.jetpackcomposedemos.features.auth.data.storage.TokenStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

// this is the one place that knows who is signed in
@Singleton
class SessionManager @Inject constructor(
    private val store: TokenStore,
    @ApplicationScope private val scope: CoroutineScope
) {

    private val _state = MutableStateFlow<AuthState>(AuthState.Restoring)
    val state: StateFlow<AuthState> = _state.asStateFlow()

    // utilities for non-composables (e.g. interceptors, repositories)
    val currentUser: User?
        get() = (_state.value as? AuthState.Authenticated)?.user

    init {
        scope.launch {
            _state.collect { current ->
                when (current) {
                    is AuthState.Authenticated -> store.save(current.user)
                    is AuthState.Unauthenticated -> store.clear()
                    is AuthState.Restoring -> Unit
                }
            }
        }
    }

    // called once at startup
    suspend fun restore() {
        if (_state.value !is AuthState.Restoring) return

        val saved = store.load()
        _state.value = if (saved != null) AuthState.Authenticated(saved) else AuthState.Unauthenticated
    }

    fun signIn(user: User) {
        _state.value = AuthState.Authenticated(user)
    }

    fun update(user: User) {
        _state.value = AuthState.Authenticated(user)
    }

    fun signOut() {
        _state.value = AuthState.Unauthenticated
    }

}


