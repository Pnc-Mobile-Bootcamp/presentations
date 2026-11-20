package com.pnc.jetpackcomposedemos.core.settings

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

// A DataStore that lives in memory, so the test needs no files. The real DataStore renames a temp file
// over its target on every write, which fails on Windows when the target already exists.
private class InMemoryPreferencesDataStore : DataStore<Preferences> {
    private val state = MutableStateFlow(emptyPreferences())
    override val data: Flow<Preferences> = state

    override suspend fun updateData(transform: suspend (t: Preferences) -> Preferences): Preferences {
        val updated = transform(state.value)
        state.value = updated
        return updated
    }
}

class DataStoreSettingsRepositoryTest {

    private fun createRepository() = DataStoreSettingsRepository(InMemoryPreferencesDataStore())

    @Test
    fun `given nothing saved, when settings are read, then defaults are returned`() = runTest {
        val repository = createRepository()

        assertEquals(AppSettings(), repository.settings.first())
    }

    @Test
    fun `when the theme mode is saved, then it is read back`() = runTest {
        val repository = createRepository()

        repository.setThemeMode(ThemeMode.DARK)

        assertEquals(ThemeMode.DARK, repository.settings.first().themeMode)
    }

    @Test
    fun `when the location is switched off, then the other settings are untouched`() = runTest {
        val repository = createRepository()
        repository.setThemeMode(ThemeMode.LIGHT)

        repository.setShowArtistLocation(false)

        assertEquals(
            AppSettings(themeMode = ThemeMode.LIGHT, showArtistLocation = false),
            repository.settings.first()
        )
    }
}


