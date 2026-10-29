package com.pnc.jetpackcomposedemos.core.settings

enum class ThemeMode { SYSTEM, LIGHT, DARK }


data class AppSettings(
    val themeMode: ThemeMode = ThemeMode.SYSTEM,
    val showArtistLocation: Boolean = true
)





