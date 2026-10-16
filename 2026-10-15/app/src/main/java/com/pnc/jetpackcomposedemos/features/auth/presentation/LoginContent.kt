package com.pnc.jetpackcomposedemos.features.auth.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.pnc.jetpackcomposedemos.features.auth.domain.User

@Composable
fun LoginContent(
    uiState: LoginUiState,
    onLogin: (String, String) -> Unit,
    onLoginSuccess: (User) -> Unit = {}
) {

    var userId by remember {
        mutableStateOf("")
    }
    var passcode by remember {
        mutableStateOf("")
    }

    val canLogin = userId.isNotBlank() && passcode.isNotBlank()

    Column(
        modifier = Modifier.fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = userId,
            onValueChange = { userId = it },
            label = { Text("User ID")}
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = passcode,
            onValueChange = { passcode = it },
            label = { Text("Passcode")},
            visualTransformation = PasswordVisualTransformation()
        )
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = {
                onLogin(userId, passcode)
            },
            enabled = canLogin
        ) {
            Text("Login")
        }

        Spacer(modifier = Modifier.height(16.dp))

        when (uiState) {
            is LoginUiState.Idle -> {}
            is LoginUiState.Loading -> CircularProgressIndicator()
            is LoginUiState.Success -> {

                val user = uiState.user

                Text("Welcome, ${user.username}")
                LaunchedEffect(user) {
                    // LaunchedEffect lets us invoke possibly suspend functions
                    // from within a composable
                    onLoginSuccess(user)
                }
            }
            is LoginUiState.Error -> {
                Text(
                    "Error: ${uiState.message}",
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    }

}

