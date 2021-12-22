package com.khangle.myfitnessapp.ui.userexc.runtrack

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.*
import androidx.fragment.app.DialogFragment
import com.google.android.material.chip.Chip
import com.khangle.myfitnessapp.R
import com.khangle.myfitnessapp.common.SharePreferenceUtil
import com.khangle.myfitnessapp.model.user.UserStat
import com.khangle.myfitnessapp.ui.base.BaseDialogFragment
import com.khangle.myfitnessapp.ui.statistic.StatisticViewModel
import com.khangle.myfitnessapp.ui.stepstrack.StepTrackViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class TimeGoalDialogFragment constructor(private val completeion: (Int) -> Unit): BaseDialogFragment() {

    private lateinit var cancelChip: Chip
    private lateinit var saveStepGoal: Chip
    private lateinit var stepGoalEditText: EditText
    private var goal = 0
    private lateinit var sharePreferenceUtil: SharePreferenceUtil
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        sharePreferenceUtil = SharePreferenceUtil.getInstance(requireContext())
        val view = inflater.inflate(R.layout.fragment_step_time_goal_dialog, container, false)
        cancelChip = view.findViewById(R.id.cancelStepGoalBtn)
        saveStepGoal = view.findViewById(R.id.saveGoalBtn)
        stepGoalEditText = view.findViewById(R.id.minutesET)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadCurrentGoal()

        cancelChip.setOnClickListener {
            dismiss()
        }
        saveStepGoal.setOnClickListener {
            saveIfnew()
        }
    }

    private fun saveIfnew() {
        val goal = stepGoalEditText.text.toString().toInt()
        if (goal != this.goal) {
            sharePreferenceUtil.setTimeGoal(goal)
            completeion(goal)
        }
        dismiss()
    }

    private fun loadCurrentGoal() {
        goal = sharePreferenceUtil.getTimeGoal()
        stepGoalEditText.setText(goal.toString())
    }



}