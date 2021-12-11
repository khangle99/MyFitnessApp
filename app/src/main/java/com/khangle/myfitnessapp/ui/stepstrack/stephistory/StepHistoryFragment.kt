package com.khangle.myfitnessapp.ui.stepstrack.stephistory

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.khangle.myfitnessapp.R
import com.khangle.myfitnessapp.common.SharePreferenceUtil
import com.khangle.myfitnessapp.common.StepUtil
import com.khangle.myfitnessapp.notification.NotificationUtil
import com.khangle.myfitnessapp.ui.statistic.history.AddStatDialogFragment
import com.khangle.myfitnessapp.ui.stepstrack.StepTrackViewModel
import com.mikhaellopez.circularprogressbar.CircularProgressBar


class StepHistoryFragment constructor(private val stepTrackViewModel: StepTrackViewModel) :
    Fragment(), SensorEventListener {

    private lateinit var stepHistoryRecyclerView: RecyclerView
    private lateinit var circularProgressIndicator: CircularProgressBar
    private lateinit var currentStepTV: TextView
    private lateinit var stepHistoryListAdapter: StepHistoryListAdapter
    lateinit var progressBar: ProgressBar
    private lateinit var setGoalBtn: Chip
    private lateinit var clearHistory: ImageView
    private var currentGoal: Int = 0
    private var isFinished = false
    private lateinit var sharePreferenceUtil: SharePreferenceUtil

    private lateinit var sensorManager: SensorManager
    private lateinit var sensor: Sensor
    private var isInit = false
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_step_history, container, false)
        sharePreferenceUtil = SharePreferenceUtil.getInstance(requireContext())
        stepHistoryRecyclerView = view.findViewById(R.id.stepHistoryList)
        circularProgressIndicator = view.findViewById(R.id.currentStepTrackIndicator)
        progressBar = view.findViewById(R.id.stepProgress)
        currentStepTV = view.findViewById(R.id.currentStepTV)
        setGoalBtn = view.findViewById(R.id.setDailyGoal)
        clearHistory = view.findViewById(R.id.clearHistory)
        if(ContextCompat.checkSelfPermission(requireContext(),
                Manifest.permission.ACTIVITY_RECOGNITION) == PackageManager.PERMISSION_DENIED){
            //init for first time
             isInit = true
            sharePreferenceUtil.setStepGoal(5000) // default value
            requestPermissions( arrayOf(Manifest.permission.ACTIVITY_RECOGNITION), 99);
        }
        sensorManager =
            requireContext().getSystemService(Context.SENSOR_SERVICE) as SensorManager
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        isFinished = sharePreferenceUtil.getFinished()

        stepHistoryListAdapter = StepHistoryListAdapter()
        loadUIStep()
        stepHistoryRecyclerView.adapter = stepHistoryListAdapter
        stepHistoryRecyclerView.layoutManager = LinearLayoutManager(context)

        setGoalBtn.setOnClickListener {
            GoalDialogFragment{
                loadUIStep(it)

            }.show(childFragmentManager, "StepGoal")
        }
        stepTrackViewModel.stepHistoryList.observe(viewLifecycleOwner) {
            progressBar.visibility = View.INVISIBLE
            if(it.isNotEmpty()) {
                stepHistoryListAdapter.submitList(it)
            } else {
                Toast.makeText(context, "No Steptrack added", Toast.LENGTH_SHORT).show()
            }
        }
        clearHistory.setOnClickListener {
            progressBar.visibility = View.VISIBLE
            stepTrackViewModel.clearHistory { message ->
                if (message.id != null) {
                    Toast.makeText(
                        requireContext(),
                        "Delete thành công với id: ${message.id}",
                        Toast.LENGTH_SHORT
                    ).show()
                    stepHistoryListAdapter.submitList(listOf())
                    stepTrackViewModel.getStepHistory()
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Lỗi khi delete error: ${message.error}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                progressBar.visibility = View.INVISIBLE

            }
        }
    }

    private fun loadUIStep(goal: Int = 0) {
        currentGoal = if (goal == 0)  SharePreferenceUtil.getInstance(requireContext()).getStepGoal() else goal

        circularProgressIndicator.progressMax = currentGoal.toFloat()
        stepHistoryListAdapter.setGoal(currentGoal)
        sensorManager.unregisterListener(this)
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_FASTEST)
    }

    override fun onResume() {
        super.onResume()

        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_FASTEST)
    }

    override fun onStop() {
        super.onStop()
        sensorManager.unregisterListener(this)
    }


    override fun onSensorChanged(event: SensorEvent?) {
        var steps = 0f
        val value = event!!.values[0]
        if (isInit) {
            StepUtil.invalidatePreviousStep(requireContext(),value.toInt())
            isInit = false
        } else {
            steps = StepUtil.calculateTodayStep(requireContext(), value).toFloat()
        }

        circularProgressIndicator.setProgressWithAnimation(steps,500, DecelerateInterpolator())
        currentStepTV.text = "Current Step: ${steps}"
        if (steps.toInt() >= currentGoal && !isFinished) {
            sharePreferenceUtil.setFinished(true)
            val notification = NotificationUtil.buildGoalFinishedNotification(requireContext())
            with(NotificationManagerCompat.from(requireContext())) {
                notify(89, notification)
            }
        }

    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }


}