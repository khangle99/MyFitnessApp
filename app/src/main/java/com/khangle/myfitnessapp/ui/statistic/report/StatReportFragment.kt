package com.khangle.myfitnessapp.ui.statistic.report

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.khangle.myfitnessapp.R
import com.khangle.myfitnessapp.model.user.UserStat
import com.khangle.myfitnessapp.ui.statistic.StatisticFragment
import com.khangle.myfitnessapp.ui.statistic.StatisticViewModel


class StatReportFragment(private val viewmodel: StatisticViewModel) : Fragment() {

    private lateinit var weighHeightChart: BarChart
    private lateinit var  bmiChart: LineChart
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
            setupBMIChart(it)
            setupWHChart(it)
            setupReportTV(it)
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
        heightDataSet.color = R.color.colorAccent

        val barSpace = 0.02f
        val barWidth = 0.45f
        val groupSpace = 0.06f

        val valueFormater = object : ValueFormatter() {
            override fun getAxisLabel(value: Float, axis: AxisBase?): String {
                return statList[value.toInt()].dateString
            }
        }
        val bardata = BarData(weightDataSet,heightDataSet)

        weighHeightChart.data = bardata
        bardata.barWidth = barWidth
        weighHeightChart.groupBars(-1f, groupSpace,barSpace)
        weighHeightChart.xAxis.valueFormatter = valueFormater
        weighHeightChart.setFitBars(true)
        weighHeightChart.invalidate()

    }

    private fun setupBMIChart(statList: List<UserStat>) {

    }

    private fun setupReportTV(statList: List<UserStat>) {

    }

}