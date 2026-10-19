package com.pnc.jetpackcomposedemos.features.auth.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pnc.jetpackcomposedemos.features.auth.data.oidc.OidcClient
import com.pnc.jetpackcomposedemos.features.auth.data.oidc.OidcRedirects
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val oidc: OidcClient,
    redirects: OidcRedirects
) : ViewModel() {

    private val _uiState = MutableStateFlow<SignInUiState>(SignInUiState.Idle)
    val uiState: StateFlow<SignInUiState> = _uiState.asStateFlow()

    init {
        // Whenever the browser sends us back, look at the answer.
        viewModelScope.launch {
            redirects.redirects.collect { redirect ->
                _uiState.value = SignInUiState.Exchanging
                oidc.completeSignIn(redirect)
                    .onSuccess { user -> _uiState.value = SignInUiState.Success(user) }
                    .onFailure { _uiState.value = SignInUiState.Error(it.message ?: "Sign-in failed") }
            }
        }
    }

    /** Returns the URL for the screen to open in a Custom Tab. */
    fun startSignIn(): String {
        _uiState.value = SignInUiState.InBrowser
        return oidc.beginSignIn().toString()
    }
}


