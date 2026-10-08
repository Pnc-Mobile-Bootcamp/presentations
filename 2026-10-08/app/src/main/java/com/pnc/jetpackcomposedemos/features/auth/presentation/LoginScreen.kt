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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.pnc.jetpackcomposedemos.features.auth.domain.User

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = viewModel(),
    onLoginSuccess: (User) -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()

    var userId by remember {
        mutableStateOf("")
    }
    var passcode by remember {
        mutableStateOf("")
    }

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
        Button(onClick = {
            viewModel.login(userId, passcode)
        }) {
            Text("Login")
        }

        Spacer(modifier = Modifier.height(16.dp))

        when (uiState) {
            is LoginUiState.Idle -> {}
            is LoginUiState.Loading -> CircularProgressIndicator()
            is LoginUiState.Success -> {
                // cast uiState as LoginUiState.Success so we can get its user property
                val user = (uiState as LoginUiState.Success).user

                Text("Welcome, ${user.username}")
                LaunchedEffect(user) {
                    // LaunchedEffect lets us invoke possibly suspend functions
                    // from within a composable
                    onLoginSuccess(user)
                }
            }
            is LoginUiState.Error -> {
                Text(
                    "Error: ${(uiState as LoginUiState.Error).message}",
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}









