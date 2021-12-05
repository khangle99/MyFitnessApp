package com.khangle.myfitnessapp.ui.statistic.otherstat.report

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.khangle.myfitnessapp.R
import com.khangle.myfitnessapp.common.BMICalculator
import com.khangle.myfitnessapp.model.AppBodyStat
import com.khangle.myfitnessapp.model.BodyStat
import com.khangle.myfitnessapp.model.user.UserStat
import org.w3c.dom.Text

class StatReportActivity : AppCompatActivity() {

    private lateinit var lineChart: LineChart
    private lateinit var chartNameTV: TextView
    private lateinit var selectStat: AppBodyStat
    private lateinit var reportSumTV: TextView
    private var filterList: List<BodyStat> = listOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stat_report)
        lineChart = findViewById(R.id.line_chart)
        chartNameTV = findViewById(R.id.chartName)
        reportSumTV = findViewById(R.id.reportSumTV)
        selectStat = intent.extras?.getParcelable("stat")!!
        filterList = intent.extras?.getParcelableArrayList("filterList") ?: listOf()
        setupChart()
        chartNameTV.setText(selectStat.name + " Chart")
        reportSum()
    }

    private fun reportSum() {
        var reportStr = "7 ngày gần nhất:\n "

        val sevenRecord = filterList.take(7)
        val first = sevenRecord.first()
        val last = sevenRecord.last()
        val percent = (last.value.toFloat() / first.value.toFloat()) - 1

        reportStr += "Total percent: ${ if (percent > 0)  "+ " else ""} ${percent*100} %"
        reportSumTV.setText(reportStr)
    }

    private fun setupChart() {
        val entryList = mutableListOf<Entry>()

        filterList.forEachIndexed { index, stat ->
            entryList.add(
                Entry(
                    index.toFloat(),
                    stat.value.toFloat()
                )
            )
        }
        val valueFormater = object : ValueFormatter() {
            override fun getAxisLabel(value: Float, axis: AxisBase?): String {
                val index = value.toInt()
                if ( index >= 0 && index < filterList.size) {
                    return filterList[index].dateString
                }
                return ""
            }
        }
        lineChart.xAxis.valueFormatter = valueFormater
        val dataSet = LineDataSet(entryList, "Value (${selectStat.unit})")

        lineChart.xAxis.granularity = 1f

        lineChart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        lineChart.axisRight.isEnabled = false
        lineChart.data = LineData(dataSet)
        lineChart.description.isEnabled = false
        lineChart.invalidate()
    }
}