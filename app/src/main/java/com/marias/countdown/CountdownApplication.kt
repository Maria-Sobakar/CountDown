package com.marias.countdown

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import com.marias.countdown.data.database.DatabaseBuilder
import com.marias.countdown.screens.eventlist.EventListAdapter

class CountdownApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        DatabaseBuilder.initialize(this)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                EventListAdapter.CHANNEL_ID,
                this.getString(R.string.notification_channel),
                NotificationManager.IMPORTANCE_DEFAULT
            )
            channel.setShowBadge(true)

            val notificationManager = this.getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }
}