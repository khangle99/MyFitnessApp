package com.khangle.myfitnessapp.ui.userexc.runtrack

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.location.LocationManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import androidx.fragment.app.viewModels
import com.google.android.material.chip.Chip
import com.khangle.myfitnessapp.R
import com.mikhaellopez.circularprogressbar.CircularProgressBar
import dagger.hilt.android.AndroidEntryPoint
import android.os.CountDownTimer
import android.widget.*
import androidx.core.content.ContextCompat
import com.khangle.myfitnessapp.common.SharePreferenceUtil
import com.khangle.myfitnessapp.common.roundTo
import android.content.Context.LOCATION_SERVICE
import android.location.Location
import android.location.LocationListener
import com.github.anastr.speedviewlib.SpeedView


@AndroidEntryPoint
class RunTrackFragment : Fragment(), SensorEventListener, LocationListener {

    private val viewModel: RunTrackViewModel by viewModels()

    private val MIN_DISTANCE_CHANGE_FOR_UPDATES = 1f
    private val MIN_TIME_BW_UPDATES = 1000L

    private lateinit var secondProgressBar: CircularProgressBar
    private lateinit var speedView: SpeedView
    private lateinit var locationManager: LocationManager
    private lateinit var currentTV: TextView
    private lateinit var playBtn: Chip
    private lateinit var changeTargetTime: Chip
    lateinit var progressBar: ProgressBar
    private lateinit var caloTV: TextView
    private lateinit var speedTV: TextView
    private lateinit var typeSpinner: Spinner

    private lateinit var sharePreferenceUtil: SharePreferenceUtil
    private lateinit var sensorManager: SensorManager
    private lateinit var sensor: Sensor

    private var isRunning = false
    private var startStep = 0L
    private var currentGoalMinutes: Int = 0
    val typeArray = arrayOf("Đi bộ", "Chạy bền", "Chạy nhanh")
    val caloFactors = arrayOf(0.105, 0.122, 0.402)

    private var selectTypeIndex = 0
    private var currentTime = 0L
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel.fetchCurrentWeight()
        val view = inflater.inflate(R.layout.fragment_run_track, container, false)
        sensorManager =
            requireContext().getSystemService(Context.SENSOR_SERVICE) as SensorManager
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
        secondProgressBar = view.findViewById(R.id.currentStepRunTrackIndicator)
        sharePreferenceUtil = SharePreferenceUtil.getInstance(requireContext())
        caloTV = view.findViewById(R.id.runCalorTV)
        speedView = view.findViewById(R.id.runSpeedView)
        playBtn = view.findViewById(R.id.playBTn)
        speedTV = view.findViewById(R.id.speedTV)
        typeSpinner = view.findViewById(R.id.runTypeSpinner)
        changeTargetTime = view.findViewById(R.id.changeTime)
        progressBar = view.findViewById(R.id.stepProgress)
        currentTV = view.findViewById(R.id.currentStepRunTV)
        currentGoalMinutes = sharePreferenceUtil.getTimeGoal()
        loadUIStep(currentGoalMinutes)
        if(ContextCompat.checkSelfPermission(requireContext(),
                Manifest.permission.ACTIVITY_RECOGNITION) == PackageManager.PERMISSION_DENIED){
            //init for first time
            sharePreferenceUtil.setTimeGoal(20) // default value
            requestPermissions( arrayOf(Manifest.permission.ACTIVITY_RECOGNITION), 99);
        }


        setupSpinner()

        changeTargetTime.setOnClickListener {
            TimeGoalDialogFragment{ minutes ->
                loadUIStep(minutes)

            }.show(childFragmentManager, "TimeGoal")
        }

        playBtn.setOnClickListener {
            if (isRunning) return@setOnClickListener
            val a = currentGoalMinutes
            isRunning = true
            typeSpinner.isEnabled = false
            object : CountDownTimer(currentGoalMinutes*1000L*60, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                   // mTextField.setText("seconds remaining: " + millisUntilFinished / 1000)
                    currentTime = (currentGoalMinutes*1000L*60 - millisUntilFinished)/1000
                    secondProgressBar.setProgressWithAnimation(secondProgressBar.progressMax - (millisUntilFinished / 1000).toFloat(),500, DecelerateInterpolator())
                    //here you can have your logic to set text to edittext
                }

                override fun onFinish() {
                    //mTextField.setText("done!")
                    typeSpinner.isEnabled = true
                    Toast.makeText(requireContext(),"Finish!", Toast.LENGTH_SHORT).show()
                    isRunning = false
                }
            }.start()
        }

        setupGPS()

        return view
    }

    private fun setupGPS() {
        locationManager = requireContext().getSystemService(LOCATION_SERVICE) as LocationManager
        if(ContextCompat.checkSelfPermission(requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED){
            //init for first time

            requestPermissions( arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION), 89);
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
    }

    private fun setupSpinner() {

        val adapter = ArrayAdapter(requireContext(),android.R.layout.simple_spinner_item, typeArray)
        typeSpinner.adapter = adapter
        typeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long,
            ) {

                selectTypeIndex = position
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

        }
    }


    private fun loadUIStep(goalMinute: Int = 0) {
        currentGoalMinutes = if (goalMinute == 0)  SharePreferenceUtil.getInstance(requireContext()).getTimeGoal() else goalMinute

        secondProgressBar.progressMax = currentGoalMinutes.toFloat() * 60 // ra giay
        sensorManager.unregisterListener(this)
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_FASTEST)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (!isRunning) {

            startStep = event!!.values[0].toLong()
            sharePreferenceUtil.setStartStep(startStep.toInt())
        } else {
            val value = event!!.values[0]
            val startStep = sharePreferenceUtil.getStartStep()
            currentTV.text = "Current Step: ${value - startStep}"
            calculateCalo()
        }

    }

    private fun calculateCalo() {
        val factor = caloFactors[selectTypeIndex]
        val currentWeight = viewModel.currentWeight.value ?: return

        caloTV.setText("Calories: ${factor*currentWeight*currentTime.toDouble().roundTo(2)}")
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {

    }

    override fun onResume() {
        super.onResume()

        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_FASTEST)
    }

    override fun onStop() {
        super.onStop()
        sensorManager.unregisterListener(this)
    }

    override fun onLocationChanged(location: Location) {
        if (location != null) {
            speedView.speedTo(location.speedAccuracyMetersPerSecond)
            speedTV.setText(location.speedAccuracyMetersPerSecond.toString())
        }
    }



}