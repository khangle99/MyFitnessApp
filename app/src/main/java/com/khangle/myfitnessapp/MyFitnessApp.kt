package com.khangle.myfitnessapp

import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import com.khangle.myfitnessapp.common.SharePreferenceUtil
import com.khangle.myfitnessapp.common.StepUtil
import com.khangle.myfitnessapp.notification.NotificationUtil.createNotificationChanel
import com.khangle.myfitnessapp.service.StepTrackUploadService
import dagger.hilt.android.HiltAndroidApp
import java.util.*
import javax.inject.Inject

private const val TAG = "MyFitnessApp"
@RequiresApi(Build.VERSION_CODES.O)
@HiltAndroidApp
class MyFitnessApp: Application(), Configuration.Provider {

    private var alarmMgr: AlarmManager? = null
    private lateinit var alarmIntent: PendingIntent


    override fun onCreate() {
        super.onCreate()
        createNotificationChanel(applicationContext)
        scheduleAlarmUploadStep()

    }

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    override fun getWorkManagerConfiguration() =
        Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()


    // daily 11h30 pm upload
    private fun scheduleAlarmUploadStep() {
        val calendar = buildTimeCalendar()
        if (calendar.timeInMillis < System.currentTimeMillis()) return
        alarmMgr = applicationContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmIntent = Intent(applicationContext, StepTrackUploadService::class.java).let { intent ->
            PendingIntent.getForegroundService(applicationContext, 0, intent, 0)
        }

        alarmMgr?.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            alarmIntent
        )
    }

    private fun buildTimeCalendar(): Calendar {
        val calendar = Calendar.getInstance()
        calendar[Calendar.HOUR_OF_DAY] = 23
        calendar[Calendar.MINUTE] = 0
        calendar[Calendar.SECOND] = 0
        return calendar
    }




}