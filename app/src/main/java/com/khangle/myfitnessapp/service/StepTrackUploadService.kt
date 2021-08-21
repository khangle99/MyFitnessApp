package com.khangle.myfitnessapp.service

import android.app.PendingIntent
import android.app.Service
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.icu.util.Calendar
import android.os.Build
import android.os.IBinder
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.work.*
import com.google.firebase.auth.FirebaseAuth
import com.khangle.myfitnessapp.MyFitnessApp
import com.khangle.myfitnessapp.backgroundwork.RecordStepTrackWorker
import com.khangle.myfitnessapp.common.ConnectivityUtil
import com.khangle.myfitnessapp.common.SharePreferenceUtil
import com.khangle.myfitnessapp.common.StepUtil
import com.khangle.myfitnessapp.data.MyFitnessAppRepository
import com.khangle.myfitnessapp.model.user.UserStep
import com.khangle.myfitnessapp.notification.NotificationUtil
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import java.util.*
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

@AndroidEntryPoint
class StepTrackUploadService : Service(), SensorEventListener {

    @Inject lateinit var repository: MyFitnessAppRepository
    private lateinit var sensorManager: SensorManager
    private lateinit var sensor: Sensor

    private var coroutineJob: Job = Job()
    val scope = CoroutineScope(Dispatchers.IO + coroutineJob)

    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        sensorManager =
            applicationContext.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_FASTEST)
        val notification = NotificationUtil.buildUploadStepNotification(applicationContext)
        startForeground(99, notification)
        return START_REDELIVER_INTENT
    }

    override fun onDestroy() {
        super.onDestroy()

        scope.cancel()
    }

    var isUploading = false
    override fun onSensorChanged(event: SensorEvent?) {
        if (isUploading) return
        val value = event!!.values[0]

        val steps = StepUtil.calculateTodayStep(applicationContext, value)
        StepUtil.invalidatePreviousStep(applicationContext, value.toInt())

        val uid = FirebaseAuth.getInstance().uid!!
        val calendar = Calendar.getInstance()
        val dateString = composeString(calendar)
        isUploading = true
        if (ConnectivityUtil.isInternetAvailable(applicationContext)) {

            scope.launch {
                val userStep = UserStep("", dateString, steps)
                val res = repository.uploadStep(uid,userStep)
                sensorManager.unregisterListener(this@StepTrackUploadService)
                stopForeground(true)
                stopSelf()
            }
        } else {
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()
            val myUploadWork = OneTimeWorkRequestBuilder<RecordStepTrackWorker>()
                .setConstraints(constraints)
                .setInputData(workDataOf(
                    "uid" to uid,
                    "dateString" to dateString,
                    "steps" to steps.toInt()
                ))
                .build()
            WorkManager.getInstance(baseContext).enqueueUniqueWork("UploadSteps", ExistingWorkPolicy.KEEP , myUploadWork)
        }
    }


    private fun composeString(calendar: Calendar): String {
        return "${calendar.get(Calendar.DAY_OF_MONTH)}/${calendar.get(Calendar.MONTH) + 1}/ ${calendar.get(Calendar.YEAR)}"
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }

}