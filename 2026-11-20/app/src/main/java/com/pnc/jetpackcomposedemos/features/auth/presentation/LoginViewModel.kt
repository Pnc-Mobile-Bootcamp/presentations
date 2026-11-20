package com.pnc.jetpackcomposedemos.features.auth.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pnc.jetpackcomposedemos.features.auth.domain.LoginUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoginViewModel(
    private val loginUseCase: LoginUseCase = LoginUseCase()
): ViewModel() {

    private val _uiState = MutableStateFlow<LoginUiState>(LoginUiState.Idle)

    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun login(userId: String, passcode: String) {

        // we inherited viewModelScope from ViewModel
        // (this allows us to launch suspendable things from a non-suspendable function)
        viewModelScope.launch {
            _uiState.value = LoginUiState.Loading

            // execute our use case
            loginUseCase(userId, passcode)
                .onSuccess { user ->
                    _uiState.value = LoginUiState.Success(user)
                }
                .onFailure { error ->
                    _uiState.value = LoginUiState.Error(error.message ?: "Unknown error")
                }
        }

    }


}