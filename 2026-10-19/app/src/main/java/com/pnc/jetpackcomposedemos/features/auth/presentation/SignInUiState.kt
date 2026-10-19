package com.pnc.jetpackcomposedemos.features.auth.presentation

import com.pnc.jetpackcomposedemos.features.auth.domain.User

sealed interface SignInUiState {
    data object Idle : SignInUiState
    data object InBrowser : SignInUiState
    data object Exchanging : SignInUiState
    data class Success(val user: User) : SignInUiState
    data class Error(val message: String) : SignInUiState
}


