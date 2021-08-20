package com.khangle.myfitnessapp

import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.SystemClock
import androidx.annotation.RequiresApi
import com.khangle.myfitnessapp.service.StepTrackUploadService
import dagger.hilt.android.HiltAndroidApp

@RequiresApi(Build.VERSION_CODES.O)
@HiltAndroidApp
class MyFitnessApp: Application() {

    private var alarmMgr: AlarmManager? = null
    private lateinit var alarmIntent: PendingIntent


    override fun onCreate() {
        super.onCreate()
        scheduleAlarmUploadStep()
    }

    private fun scheduleAlarmUploadStep() {

        alarmMgr = applicationContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmIntent = Intent(applicationContext, StepTrackUploadService::class.java).let { intent ->
            PendingIntent.getForegroundService(applicationContext, 0, intent, 0)
        }

        alarmMgr?.setExact(
            AlarmManager.RTC_WAKEUP,
             System.currentTimeMillis() + 1 * 1000,
            alarmIntent
        )
    }
}