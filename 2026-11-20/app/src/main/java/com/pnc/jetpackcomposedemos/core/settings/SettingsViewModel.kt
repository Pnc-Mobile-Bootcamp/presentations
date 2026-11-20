package com.pnc.jetpackcomposedemos.core.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pnc.jetpackcomposedemos.core.observability.CrashReporter
import com.pnc.jetpackcomposedemos.core.sync.ArtistSyncScheduler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val repository: SettingsRepository,
    private val syncScheduler: ArtistSyncScheduler,
    private val crashReporter: CrashReporter
): ViewModel() {

    // the repository gives us a cold Flow from the DataStore
    // turn that into state the UI can read
    // (the initialValue will show until the DataStore file is read)
    val settings: StateFlow<AppSettings> = repository.settings.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = AppSettings()
    )

    fun setThemeMode(mode: ThemeMode) {
        viewModelScope.launch { repository.setThemeMode(mode) }
    }

    fun setShowArtistLocation(show: Boolean) {
        viewModelScope.launch { repository.setShowArtistLocation(show) }
    }

    // remember the notification choice AND start or stop the hourly sync
    // that sends the notification
    fun setNotifyNewArtists(enabled: Boolean) {
        viewModelScope.launch { repository.setNotifyNewArtists(enabled) }
        if (enabled) syncScheduler.schedule() else syncScheduler.cancel()
    }

    fun syncNow() = syncScheduler.syncNow()

    // debug diagnostics
    fun latestReport(): String? = crashReporter.latestReport()

    fun recordTestError() {
        Timber.e(
            IllegalStateException("Test error, token=abc123 for someone@example.com"),
            "Handled test error"
        )
    }

}



