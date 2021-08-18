package com.khangle.myfitnessapp.ui.stepstrack.report

import android.content.Context
import android.hardware.*
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat.getSystemService
import com.github.mikephil.charting.charts.LineChart
import com.khangle.myfitnessapp.R
import com.khangle.myfitnessapp.ui.stepstrack.StepTrackViewModel


class StepReportFragment constructor(private val stepTrackViewModel: StepTrackViewModel): Fragment() {

    private lateinit var stepChar: LineChart
    private lateinit var reportTV: TextView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_step_report, container, false)
        stepChar = view.findViewById(R.id.stepchart)
        reportTV = view.findViewById(R.id.stepReportTV)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }



}