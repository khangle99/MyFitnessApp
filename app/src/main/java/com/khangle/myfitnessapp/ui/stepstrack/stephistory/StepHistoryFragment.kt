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
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.khangle.myfitnessapp.R
import com.khangle.myfitnessapp.ui.stepstrack.StepTrackViewModel
import com.mikhaellopez.circularprogressbar.CircularProgressBar


class StepHistoryFragment constructor(private val stepTrackViewModel: StepTrackViewModel) :
    Fragment(), SensorEventListener {

    private lateinit var stepHistoryRecyclerView: RecyclerView
    private lateinit var circularProgressIndicator: CircularProgressBar
    private lateinit var currentStepTV: TextView
    private lateinit var stepHistoryListAdapter: StepHistoryListAdapter
    lateinit var progressBar: ProgressBar

    private lateinit var sensorManager: SensorManager
    private lateinit var sensor: Sensor

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_step_history, container, false)
        stepHistoryRecyclerView = view.findViewById(R.id.stepHistoryList)
        circularProgressIndicator = view.findViewById(R.id.currentStepTrackIndicator)
        progressBar = view.findViewById(R.id.stepProgress)
        currentStepTV = view.findViewById(R.id.currentStepTV)
        if(ContextCompat.checkSelfPermission(requireContext(),
                Manifest.permission.ACTIVITY_RECOGNITION) == PackageManager.PERMISSION_DENIED){
            //ask for permission
            requestPermissions( arrayOf(Manifest.permission.ACTIVITY_RECOGNITION), 99);
        }
        sensorManager =
            requireContext().getSystemService(Context.SENSOR_SERVICE) as SensorManager
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        circularProgressIndicator.progressMax = 2500f

        stepHistoryListAdapter = StepHistoryListAdapter()
        stepHistoryRecyclerView.adapter = stepHistoryListAdapter
        stepHistoryRecyclerView.layoutManager = LinearLayoutManager(context)

        stepTrackViewModel.stepHistoryList.observe(viewLifecycleOwner) {
            stepHistoryListAdapter.submitList(it)
            progressBar.visibility = View.INVISIBLE
        }
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
        val steps = event!!.values[0]
        circularProgressIndicator.progress = steps
        currentStepTV.text = "Current Step: ${steps}"
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }


}