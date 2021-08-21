package com.khangle.myfitnessapp.notification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.khangle.myfitnessapp.MyFitnessApp
import com.khangle.myfitnessapp.R

object NotificationUtil {
    fun createNotificationChanel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(UPLOAD_CHANNEL, "Channel for upload Step record", NotificationManager.IMPORTANCE_LOW)
            context.getSystemService(NotificationManager::class.java).createNotificationChannel(channel)
        }
    }

    fun buildUploadStepNotification(context: Context): Notification {
        val pendingIntent = PendingIntent.getActivity(context,0, Intent(), PendingIntent.FLAG_UPDATE_CURRENT)
        return NotificationCompat.Builder(context, UPLOAD_CHANNEL)
            .setContentTitle("Record User Step Track")
            .setOngoing(false)
            .setAutoCancel(true)
            .setContentText("Recording...")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentIntent(pendingIntent)
            .build()
    }

    val UPLOAD_CHANNEL = "com.khangle.myfitnessapp.uploadchannel"

}