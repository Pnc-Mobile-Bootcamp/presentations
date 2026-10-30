package com.pnc.jetpackcomposedemos.core.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
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
        onShowArtistLocationChange = viewModel::setShowArtistLocation
    )
}

@Composable
fun SettingsContent(
    settings: AppSettings,
    onThemeModeChange: (ThemeMode) -> Unit,
    onShowArtistLocationChange: (Boolean) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
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
    }
}