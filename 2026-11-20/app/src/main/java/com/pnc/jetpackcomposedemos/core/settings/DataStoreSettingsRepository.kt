package com.pnc.jetpackcomposedemos.core.settings

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataStoreSettingsRepository @Inject constructor(
    @SettingsDataStore private val dataStore: DataStore<Preferences>
): SettingsRepository {

    private object Keys {
        val THEME_MODE = stringPreferencesKey("theme_mode")
        val SHOW_ARTIST_LOCATION = booleanPreferencesKey("show_artist_location")
        val NOTIFY_NEW_ARTISTS = booleanPreferencesKey("notify_new_artists")
    }

    override suspend fun setThemeMode(mode: ThemeMode) {
        dataStore.edit { it[Keys.THEME_MODE] = mode.name }
    }

    override suspend fun setShowArtistLocation(show: Boolean) {
        dataStore.edit { it[Keys.SHOW_ARTIST_LOCATION] = show }
    }

    override suspend fun setNotifyNewArtists(enabled: Boolean) {
        dataStore.edit { it[Keys.NOTIFY_NEW_ARTISTS] = enabled }
    }

    override val settings: Flow<AppSettings> = dataStore.data.map { prefs ->
        AppSettings(
            themeMode = prefs[Keys.THEME_MODE]
                ?.let { name -> ThemeMode.entries.firstOrNull { it.name == name } }
                ?: ThemeMode.SYSTEM,
            showArtistLocation = prefs[Keys.SHOW_ARTIST_LOCATION] ?: true,
            notifyNewArtists = prefs[Keys.NOTIFY_NEW_ARTISTS] ?: false
        )
    }

}



