package com.pnc.jetpackcomposedemos.features.auth.presentation

import androidx.lifecycle.ViewModel
import com.pnc.jetpackcomposedemos.features.auth.domain.AuthState
import com.pnc.jetpackcomposedemos.features.auth.domain.SessionManager
import com.pnc.jetpackcomposedemos.features.auth.domain.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val session: SessionManager
): ViewModel() {

    val uiState: StateFlow<AuthState> = session.state

    init {
        viewModelScope.launch {
            session.restore()
        }
    }

    fun login(user: User) = session.signIn(user)

    fun logout() = session.signOut()

}






