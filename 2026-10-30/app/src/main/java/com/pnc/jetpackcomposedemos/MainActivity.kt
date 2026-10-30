package com.pnc.jetpackcomposedemos


import android.app.Application
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.pnc.jetpackcomposedemos.core.structure.EventPlannerApp
import com.pnc.jetpackcomposedemos.core.settings.LocalAppSettings
import com.pnc.jetpackcomposedemos.core.settings.SettingsViewModel
import com.pnc.jetpackcomposedemos.core.settings.ThemeMode
import com.pnc.jetpackcomposedemos.features.auth.data.oidc.OidcRedirects
import com.pnc.jetpackcomposedemos.ui.theme.KazooTheme
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class EventPlannerApplication: Application()

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var oidcRedirects: OidcRedirects

    private val settingsViewModel: SettingsViewModel by viewModels()

    private companion object {
        val LIGHT_SCRIM = android.graphics.Color.argb(0xe6, 0xFF, 0xFF, 0xFF)
        val DARK_SCRIM = android.graphics.Color.argb(0x80, 0x1b, 0x1b, 0x1b)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)

        val uri = intent.dataString
        if (uri != null && uri.startsWith(BuildConfig.OIDC_REDIRECT_URI)) {
            oidcRedirects.deliver(uri)
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val settings by settingsViewModel.settings.collectAsStateWithLifecycle()
            val darkTheme = when (settings.themeMode) {
                ThemeMode.LIGHT -> false
                ThemeMode.DARK -> true
                ThemeMode.SYSTEM -> isSystemInDarkTheme()
            }
            // enableEdgeToEdge() picked the status and navigation bar icon colors
            // from the SYSTEM theme when the activity was created
            // Now that our app's own setting might disagree with the system
            // set them again whenever the chosen theme changes
            DisposableEffect(darkTheme) {
                enableEdgeToEdge(
                    statusBarStyle = SystemBarStyle.auto(
                        lightScrim = android.graphics.Color.TRANSPARENT,
                        darkScrim = android.graphics.Color.TRANSPARENT
                    ) { darkTheme },
                    navigationBarStyle = SystemBarStyle.auto(
                        lightScrim = LIGHT_SCRIM,
                        darkScrim = DARK_SCRIM
                    ) { darkTheme }
                )
                onDispose {}
            }
            CompositionLocalProvider(LocalAppSettings provides settings) {
                KazooTheme(darkTheme = darkTheme) {
                    Surface(modifier = Modifier.fillMaxSize()) {
                        EventPlannerApp()
                    }
                }
            }




        }
    }
}



