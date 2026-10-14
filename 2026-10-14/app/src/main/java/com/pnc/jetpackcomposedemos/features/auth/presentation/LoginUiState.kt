package com.pnc.jetpackcomposedemos.features.auth.presentation

import com.pnc.jetpackcomposedemos.features.auth.domain.User

sealed interface LoginUiState {

    data object Idle: LoginUiState
    data object Loading: LoginUiState
    data class Success(val user: User): LoginUiState
    data class Error(val message: String): LoginUiState

}


