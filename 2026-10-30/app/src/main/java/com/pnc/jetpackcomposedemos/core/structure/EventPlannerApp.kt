package com.pnc.jetpackcomposedemos.core.structure

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfoV2
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import androidx.window.core.layout.WindowSizeClass
import com.pnc.jetpackcomposedemos.features.auth.domain.AuthState
import com.pnc.jetpackcomposedemos.features.auth.presentation.AuthViewModel
import com.pnc.jetpackcomposedemos.features.auth.presentation.SignInScreen

@Composable
fun EventPlannerApp(
    viewModel: AuthViewModel = hiltViewModel()
) {
    val authState by viewModel.uiState.collectAsStateWithLifecycle()

    val windowSizeClass = currentWindowAdaptiveInfoV2().windowSizeClass
    val useTwoPaneLayout = windowSizeClass.isWidthAtLeastBreakpoint(
        WindowSizeClass.WIDTH_DP_EXPANDED_LOWER_BOUND
    )

    val navController = rememberNavController()


    when (authState) {
        is AuthState.Restoring -> {
            FullScreenBox {
                CircularProgressIndicator()
            }
        }
        is AuthState.Unauthenticated -> {
            FullScreenBox {
                SignInScreen(
                    onSignIn = { user -> viewModel.login(user) }
                )
            }
        }
        is AuthState.Authenticated -> {
            AppNavigationShell(navController = navController) {
                AppNavHost(
                    navController = navController,
                    useTwoPaneLayout = useTwoPaneLayout,
                    onSignOut = {
                        viewModel.logout()
                    }
                )
            }
        }
    }

}



