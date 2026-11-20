package com.pnc.jetpackcomposedemos.features.auth.presentation

import com.pnc.jetpackcomposedemos.features.auth.domain.User

sealed interface SignInState {

    data object Idle: SignInState
    data object InBrowser: SignInState
    data object Exchanging: SignInState
    data class Success(val user: User): SignInState
    data class Error(val message: String): SignInState

}


