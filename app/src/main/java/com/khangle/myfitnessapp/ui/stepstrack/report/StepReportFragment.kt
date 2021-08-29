package com.khangle.myfitnessapp.ui.stepstrack.report

import android.content.Context
import android.graphics.Color
import android.hardware.*
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.TextUtils
import android.text.style.ForegroundColorSpan
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.khangle.myfitnessapp.R
import com.khangle.myfitnessapp.common.BMICalculator
import com.khangle.myfitnessapp.common.SharePreferenceUtil
import com.khangle.myfitnessapp.model.user.UserStat
import com.khangle.myfitnessapp.model.user.UserStep
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
        stepTrackViewModel.stepHistoryList.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                setupStepHistoryChart(it)
                setupReportTV(it)
            } else {
                Toast.makeText(context, "No StepTrack added", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupStepHistoryChart(stepList: List<UserStep>) {
        val entryList = mutableListOf<Entry>()

        stepList.forEachIndexed { index, step ->
            entryList.add(
                Entry(
                    index.toFloat(),
                    step.steps.toFloat()
                )
            )
        }
        val valueFormater = object : ValueFormatter() {
            override fun getAxisLabel(value: Float, axis: AxisBase?): String {
                val index = value.toInt()
                if ( index >= 0 && index < stepList.size) {
                    return stepList[index].dateString
                }
                return ""
            }
        }
        stepChar.xAxis.valueFormatter = valueFormater
        val dataSet = LineDataSet(entryList, "Step")

        stepChar.xAxis.granularity = 1f

        stepChar.xAxis.position = XAxis.XAxisPosition.BOTTOM
        stepChar.axisRight.isEnabled = false
        stepChar.data = LineData(dataSet)
        stepChar.description.isEnabled = false
        stepChar.invalidate()
    }

    private fun setupReportTV(stepList: List<UserStep>) {
        val firstStep = stepList.first()
        val lastStep = stepList.last()
        val stepPercent = (lastStep.steps*1.0/firstStep.steps)*100 - 100
        val goal = SharePreferenceUtil.getInstance(requireContext()).getStepGoal()
        var countSuccessGoal = 0
        stepList.forEach {
            if (it.steps >= goal) countSuccessGoal++
        }

        val composeStr = TextUtils.concat(" Steps: ", composeSpanString(stepPercent.toInt()) , "\n Goal Days: ${countSuccessGoal}")
        reportTV.text = composeStr
    }

    private fun composeSpanString(number: Int): SpannableString {
        val spannable: SpannableString
        if (number < 0) {
            spannable = SpannableString("${number}%")
            spannable.setSpan(
                ForegroundColorSpan(Color.RED),
                0, // start
                spannable.length , // end
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        } else {
            spannable = SpannableString("+${number}%")
            spannable.setSpan(
                ForegroundColorSpan(Color.GREEN),
                0, // start
                spannable.length , // end
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }

        return spannable
    }

}