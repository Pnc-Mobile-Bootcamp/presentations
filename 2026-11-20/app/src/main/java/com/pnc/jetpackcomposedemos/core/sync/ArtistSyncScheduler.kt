package com.pnc.jetpackcomposedemos.core.sync

import android.content.Context
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ArtistSyncScheduler @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private companion object {
        const val PERIODIC_NAME = "artist-sync"
        const val ONE_TIME_NAME = "artist-sync-now"
    }

    private val onlyWithNetwork = Constraints.Builder()
        .setRequiredNetworkType(NetworkType.CONNECTED)
        .build()

    // sync once an hour while a network is available
    // (this is a safe call, b/c calling twice keeps ONE schedule, not two)
    fun schedule() {
        val request = PeriodicWorkRequestBuilder<ArtistSyncWorker>(1, TimeUnit.HOURS)
            .setConstraints(onlyWithNetwork)
            .build()

        WorkManager.getInstance(context)
            .enqueueUniquePeriodicWork(PERIODIC_NAME, ExistingPeriodicWorkPolicy.UPDATE, request)
    }

    fun cancel() {
        WorkManager.getInstance(context).cancelUniqueWork(PERIODIC_NAME)
    }

    // runs one sync now. Handy for demonstrations
    fun syncNow() {
        val request = OneTimeWorkRequestBuilder<ArtistSyncWorker>()
            .setConstraints(onlyWithNetwork)
            .build()

        WorkManager.getInstance(context)
            .enqueueUniqueWork(ONE_TIME_NAME, ExistingWorkPolicy.REPLACE, request)
    }

}


