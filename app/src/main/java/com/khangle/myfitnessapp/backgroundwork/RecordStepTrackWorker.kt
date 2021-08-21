package com.khangle.myfitnessapp.backgroundwork

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkerParameters
import com.khangle.myfitnessapp.common.StepUtil
import com.khangle.myfitnessapp.data.MyFitnessAppRepository
import com.khangle.myfitnessapp.model.user.UserStep
import com.khangle.myfitnessapp.notification.NotificationUtil
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

private const val TAG = "RecordStepTrackWorker"
@HiltWorker
class RecordStepTrackWorker @AssistedInject constructor(private val repository: MyFitnessAppRepository, @Assisted val context: Context, @Assisted parameters: WorkerParameters) :
    CoroutineWorker(context, parameters) {

    override suspend fun doWork(): Result {
        setForeground(createForegroundInfo())
        val uid = inputData.getString("uid")!!
        val dateString = inputData.getString("dateString")!!
        val steps = inputData.getInt("steps",0)
        val userStep = UserStep("", dateString, steps)
        val res = repository.uploadStep(uid, userStep)

        Log.i(TAG, "doWork: done upload with stepid ${res.id}")
        return Result.success()
    }

    private fun createForegroundInfo(): ForegroundInfo {
        val notification = NotificationUtil.buildUploadStepNotification(context)
        return ForegroundInfo(99, notification)
    }
}