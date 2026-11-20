package com.pnc.jetpackcomposedemos.core.settings

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.pnc.jetpackcomposedemos.BuildConfig
import com.pnc.jetpackcomposedemos.R
import com.pnc.jetpackcomposedemos.ui.theme.LocalSpacing

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val settings by viewModel.settings.collectAsStateWithLifecycle()
    SettingsContent(
        settings = settings,
        onThemeModeChange = viewModel::setThemeMode,
        onShowArtistLocationChange = viewModel::setShowArtistLocation,
        onNotifyNewArtistsChange = viewModel::setNotifyNewArtists,
        onSyncNow = viewModel::syncNow,
        onRecordTestError = viewModel::recordTestError,
        latestReport = viewModel::latestReport
    )
}

@Composable
fun SettingsContent(
    settings: AppSettings,
    onThemeModeChange: (ThemeMode) -> Unit,
    onShowArtistLocationChange: (Boolean) -> Unit,
    onNotifyNewArtistsChange: (Boolean) -> Unit,
    onSyncNow: () -> Unit,
    onRecordTestError: () -> Unit = {},
    latestReport: () -> String? = { null }
) {
    val context = LocalContext.current

    // True after the user said "no" to the permission dialog.
    // Android stops asking after a couple of refusals,
    // so from then on, the only way forward is the system settings screen
    var permissionDenied by remember { mutableStateOf(false) }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {
        granted ->
        permissionDenied = !granted
        onNotifyNewArtistsChange(granted)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(LocalSpacing.current.large),
        verticalArrangement = Arrangement.spacedBy(LocalSpacing.current.large)
    ) {
        Text(
            text = stringResource(R.string.settings_title),
            style = MaterialTheme.typography.headlineMedium
        )

        Text(
            text = stringResource(R.string.settings_theme),
            style = MaterialTheme.typography.titleLarge
        )
        Column(Modifier.selectableGroup()) {
            ThemeMode.entries.forEach { mode ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .selectable(
                            selected = settings.themeMode == mode,
                            onClick = { onThemeModeChange(mode) },
                            role = Role.RadioButton
                        )
                        .padding(vertical = LocalSpacing.current.small),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(selected = settings.themeMode == mode, onClick = null)
                    Text(
                        text = stringResource(
                            when (mode) {
                                ThemeMode.SYSTEM -> R.string.settings_theme_system
                                ThemeMode.LIGHT -> R.string.settings_theme_light
                                ThemeMode.DARK -> R.string.settings_theme_dark
                            }
                        ),
                        modifier = Modifier.padding(start = 12.dp)
                    )
                }
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.settings_show_location),
                modifier = Modifier.weight(1f)
            )
            Switch(
                checked = settings.showArtistLocation,
                onCheckedChange = onShowArtistLocationChange
            )
        }

        Text(
            text = stringResource(R.string.settings_notifications),
            style = MaterialTheme.typography.titleLarge
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.settings_notify_new_artists),
                modifier = Modifier.weight(1f)
            )
            Switch(
                checked = settings.notifyNewArtists,
                onCheckedChange = { wanted ->
                    when {
                        !wanted -> {
                            permissionDenied = false
                            onNotifyNewArtistsChange(false)
                        }

                        hasNotificationPermission(context) -> onNotifyNewArtistsChange(true)

                        else -> permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)

                    }
                }
            )
        }

        if (permissionDenied) {
            Text(
                text = stringResource(R.string.settings_notifications_blocked),
                color = MaterialTheme.colorScheme.error
            )
            OutlinedButton(
                onClick = { openNotificationSettings(context) }
            ) {
                Text(stringResource(R.string.settings_open_system_settings))
            }
        }

        if (settings.notifyNewArtists) {
            OutlinedButton(onClick = onSyncNow) {
                Text(stringResource(R.string.settings_sync_now))
            }
        }

        // only in debug builds: this panel is for learning,
        // release users should never see it
        if (BuildConfig.DEBUG) {
            var report by remember { mutableStateOf<String?>(null) }

            Text(
                text = stringResource(R.string.settings_diagnostics),
                style = MaterialTheme.typography.titleLarge
            )
            Column(
                verticalArrangement = Arrangement.spacedBy(LocalSpacing.current.small)
            ) {
                OutlinedButton(onClick = {
                        onRecordTestError()
                        report = latestReport()
                    }) {
                    Text(stringResource(R.string.settings_log_test_error))
                }
                OutlinedButton(onClick = { report = latestReport() }) {
                    Text(stringResource(R.string.settings_show_report))
                }
            }

            report?.let { text ->
                Text(
                    text = text,
                    style = MaterialTheme.typography.bodySmall,
                    fontFamily = FontFamily.Monospace
                )
            }

        }


    }
}

// Before Android 13 (TIRAMISU), there is no runtime permission
private fun hasNotificationPermission(context: Context): Boolean =
    Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU ||
            ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) ==
            PackageManager.PERMISSION_GRANTED

private fun openNotificationSettings(context: Context) {
    val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
        .putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    context.startActivity(intent)
}

