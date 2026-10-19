package com.pnc.jetpackcomposedemos.features.auth.presentation

import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.pnc.jetpackcomposedemos.features.auth.domain.User

@Composable
fun SignInScreen(
    viewModel: SignInViewModel = hiltViewModel(),
    onSignedIn: (User) -> Unit = {}   // used from Demo 4
) {
    val state by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = {
            val url = viewModel.startSignIn()
            CustomTabsIntent.Builder().build().launchUrl(context, url.toUri())
        }) {
            Text("Sign in")
        }

        Spacer(modifier = Modifier.height(16.dp))

        when (val current = state) {
            SignInUiState.Idle -> {}
            SignInUiState.InBrowser -> Text("Waiting for the browser...")
            SignInUiState.Exchanging -> CircularProgressIndicator()
            is SignInUiState.Success -> {
                Text("Welcome, ${current.user.username}")
                LaunchedEffect(current.user) { onSignedIn(current.user) }
            }
            is SignInUiState.Error -> Text(
                "Error: ${current.message}",
                color = MaterialTheme.colorScheme.error
            )
        }
    }
}


