package com.pnc.jetpackcomposedemos.features.auth.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pnc.jetpackcomposedemos.features.auth.data.oidc.OidcClient
import com.pnc.jetpackcomposedemos.features.auth.data.oidc.OidcRedirects
import javax.inject.Inject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val oidc: OidcClient,
    redirects: OidcRedirects
): ViewModel() {

    private val _uiState = MutableStateFlow<SignInState>(SignInState.Idle)
    val uiState: StateFlow<SignInState> = _uiState.asStateFlow()

    init {
        // whenever the browser sends us back, look at the answer
        viewModelScope.launch {
            redirects.redirects.collect { redirect ->
                _uiState.value = SignInState.Exchanging
                oidc.completeSignIn(redirect)
                    .onSuccess { user -> _uiState.value = SignInState.Success(user) }
                    .onFailure { _uiState.value = SignInState.Error(it.message ?: "Sign-in failed") }
            }
        }
    }

    fun startSignIn(): String {
        _uiState.value = SignInState.InBrowser
        return oidc.beginSignIn().toString()
    }

    // This ViewModel outlives the sign-in screen. Once the success has been handed to the
    // session, forget it - otherwise the screen would hand the same (old) user over again the
    // next time it is shown, e.g. right after signing out.
    fun onSignInHandled() {
        if (_uiState.value is SignInState.Success) {
            _uiState.value = SignInState.Idle
        }
    }

}


