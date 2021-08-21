package com.khangle.myfitnessapp.common

import android.content.Context

object StepUtil {

    fun calculateTodayStep(context: Context, sensorValue: Float): Int {
        val step = SharePreferenceUtil.getInstance(context).getPreviousDayStep()
        return sensorValue.toInt() - step
    }

    fun invalidatePreviousStep(context: Context, sensorValue: Int) {
        SharePreferenceUtil.getInstance(context).setPreviousDayStep(sensorValue)
    }
}