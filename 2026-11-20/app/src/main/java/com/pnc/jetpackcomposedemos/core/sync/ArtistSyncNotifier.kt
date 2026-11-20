package com.pnc.jetpackcomposedemos.core.sync

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import com.pnc.jetpackcomposedemos.MainActivity
import com.pnc.jetpackcomposedemos.R
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ArtistSyncNotifier @Inject constructor(
    @ApplicationContext private val context: Context
) {

    // true when we are allowed to post notifications
    // i.e. the permission is granted (Android 13+)
    // and notifications are not blocked
    fun canNotify(): Boolean {
        val permitted = Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU ||
                ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED

        return permitted && NotificationManagerCompat.from(context).areNotificationsEnabled()
    }

    companion object {
        const val CHANNEL_ID = "artist_updates"
        const val NOTIFICATION_ID = 1001
        const val ARTISTS_DEEP_LINK = "com.pnc.jetpackcomposedemos://app/artists"
    }

    // a channel is the user's switch for this kind of notification
    // (in system settings)
    private fun createChannel() {
        val channel = NotificationChannel(
            CHANNEL_ID,
            context.getString(R.string.notification_channel_name),
            NotificationManager.IMPORTANCE_DEFAULT
        )
        context.getSystemService(NotificationManager::class.java)
            .createNotificationChannel(channel)
    }

    fun notifyNewArtists(count: Int) {
        if (!canNotify()) return
        createChannel()

        // tapping the notification should open the Artists screen through the deep link
        val openArtists = Intent(
            Intent.ACTION_VIEW,
            ARTISTS_DEEP_LINK.toUri(),
            context,
            MainActivity::class.java
        ).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)

        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            openArtists,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.filled_star)
            .setContentTitle(context.getString(R.string.notification_new_artists_title))
            .setContentText(context.resources.getQuantityString(
                R.plurals.notification_new_artists_text, count, count))
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        // canNotify() above, has already checked the permission
        // but the linter cannot see that, so we tell it here
        @Suppress("MissingPermission")
        NotificationManagerCompat.from(context).notify(NOTIFICATION_ID, notification)

    }


}



