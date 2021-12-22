package com.khangle.myfitnessapp.common

import android.content.Context
import android.content.SharedPreferences

class SharePreferenceUtil constructor(private val pref: SharedPreferences) {
    val STEP_GOAL = "STEP_GOAL"
    val START_STEP = "START_STEP"
    val TIME_GOAL = "TIME_GOAL"
    val PREV_STEP = "PREV_STEP"

    fun setStepGoal(value: Int) {
        pref.edit()
            .putInt(STEP_GOAL, value)
            .apply()
    }

    fun getStepGoal(): Int {
        return pref.getInt(STEP_GOAL, 0)
    }


    fun setTimeGoal(value: Int) {
        pref.edit()
            .putInt(TIME_GOAL, value)
            .apply()
    }

    fun getTimeGoal(): Int {
        return pref.getInt(TIME_GOAL, 0)
    }

    fun setStartStep(value: Int) {
        pref.edit()
            .putInt(START_STEP, value)
            .apply()
    }

    fun getStartStep(): Int {
        return pref.getInt(START_STEP, 0)
    }

    fun setPreviousDayStep(value: Int) {
        pref.edit()
            .putInt(PREV_STEP, value)
            .apply()
    }

    fun getPreviousDayStep(): Int {
        return pref.getInt(PREV_STEP, 0)
    }

    fun remove(key: String?) {
        pref.edit()
            .remove(key)
            .apply()
    }

    fun clear(): Boolean {
        return pref.edit()
            .clear()
            .commit()
    }

    fun getFinished(): Boolean {
        return pref.getBoolean("isFinished", false)
    }

    fun setFinished(isFinish: Boolean) {
        pref.edit().putBoolean("isFinished", isFinish).apply()
    }

    companion object {
        private var _instance: SharePreferenceUtil? = null

        fun getInstance(context: Context): SharePreferenceUtil {

            if (_instance != null) return _instance!!
            val sharedPref = context.getSharedPreferences(
                "MyFitnessAppPref", Context.MODE_PRIVATE)
            val sharePreferenceUtil = SharePreferenceUtil(sharedPref)
            _instance = sharePreferenceUtil

            return _instance!!
        }
    }

}