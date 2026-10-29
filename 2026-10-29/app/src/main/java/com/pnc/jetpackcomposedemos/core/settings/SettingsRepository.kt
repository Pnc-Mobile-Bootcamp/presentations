package com.pnc.jetpackcomposedemos.core.settings

import kotlinx.coroutines.flow.Flow

interface SettingsRepository {

    val settings: Flow<AppSettings>

    suspend fun setThemeMode(mode: ThemeMode)

    suspend fun setShowArtistLocation(show: Boolean)

}



