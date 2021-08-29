package com.khangle.myfitnessapp.ui.statistic.report

import android.graphics.Color
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
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.ValueFormatter
import com.khangle.myfitnessapp.R
import com.khangle.myfitnessapp.common.BMICalculator
import com.khangle.myfitnessapp.model.user.UserStat
import com.khangle.myfitnessapp.ui.statistic.StatisticFragment
import com.khangle.myfitnessapp.ui.statistic.StatisticViewModel
import kotlin.math.abs


class StatReportFragment(private val viewmodel: StatisticViewModel) : Fragment() {

    private lateinit var weighHeightChart: BarChart
    private lateinit var bmiChart: LineChart
    private lateinit var reportTV: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_stat_report, container, false)
        bmiChart = view.findViewById(R.id.bmichart)
        weighHeightChart = view.findViewById(R.id.whchart)
        reportTV = view.findViewById(R.id.statReportTV)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewmodel.statHistoryList.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                setupBMIChart(it)
                setupWHChart(it)
                setupReportTV(it)
            } else {
                Toast.makeText(context, "No Statistic added", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupWHChart(statList: List<UserStat>) {
        val weightEntries = mutableListOf<BarEntry>()
        val heightEntries = mutableListOf<BarEntry>()

        statList.forEachIndexed { index, stat ->
            weightEntries.add(BarEntry(index.toFloat(), stat.weight.toFloat()))
            heightEntries.add(BarEntry(index.toFloat(), stat.height.toFloat()))
        }
        val weightDataSet = BarDataSet(weightEntries, "Weight")
        weightDataSet.color = R.color.colorPrimary
        val heightDataSet = BarDataSet(heightEntries, "Height")

        val barSpace = 0.02f
        val barWidth = 0.35f
        val groupSpace = 0.25f

        val valueFormater = object : ValueFormatter() {
            override fun getAxisLabel(value: Float, axis: AxisBase?): String {
                val index = value.toInt()
                if ( index >= 0 && index < statList.size) {
                    return statList[index].dateString
                }
                return ""
            }
        }
        val bardata = BarData(weightDataSet, heightDataSet)

        weighHeightChart.data = bardata
        bardata.barWidth = barWidth
        weighHeightChart.groupBars(-0.5f, groupSpace, barSpace)
        weighHeightChart.xAxis.valueFormatter = valueFormater
        weighHeightChart.xAxis.granularity = 1f


        weighHeightChart.setFitBars(true)
        weighHeightChart.description.isEnabled = false
        weighHeightChart.setDrawGridBackground(false)
        weighHeightChart.axisRight.isEnabled = false
        weighHeightChart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        weighHeightChart.invalidate()
    }

    private fun setupBMIChart(statList: List<UserStat>) {
        val entryList = mutableListOf<Entry>()

        statList.forEachIndexed { index, stat ->
            entryList.add(
                Entry(
                    index.toFloat(),
                    BMICalculator.bmiFrom(stat.weight, stat.height).toFloat()
                )
            )
        }
        val valueFormater = object : ValueFormatter() {
            override fun getAxisLabel(value: Float, axis: AxisBase?): String {
                val index = value.toInt()
                if ( index >= 0 && index < statList.size) {
                    return statList[index].dateString
                }
                return ""
            }
        }
        bmiChart.xAxis.valueFormatter = valueFormater
        val dataSet = LineDataSet(entryList, "BMI")

        bmiChart.xAxis.granularity = 1f

        bmiChart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        bmiChart.axisRight.isEnabled = false
        bmiChart.data = LineData(dataSet)
        bmiChart.description.isEnabled = false
        bmiChart.invalidate()
    }

    private fun setupReportTV(statList: List<UserStat>) {
        val firstStat = statList.first()
        val lastStat = statList.last()
        val weightPercent = (lastStat.weight*1.0 / firstStat.weight) * 100 - 100
        val heightPercent = (lastStat.height*1.0 / firstStat.height*1.0) * 100 - 100
        val firstBMI = BMICalculator.bmiFrom(firstStat.weight, firstStat.height)
        val lastBMI = BMICalculator.bmiFrom(lastStat.weight, lastStat.height)
        val bmiPercent = (lastBMI/firstBMI*1.0)*100 - 100


        val composeStr = TextUtils.concat(" Weight: ", composeSpanString(weightPercent.toInt()), "\n Height: ", composeSpanString(heightPercent.toInt()), "\n BMI: ", composeSpanString(bmiPercent.toInt()))
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