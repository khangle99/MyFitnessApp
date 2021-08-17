package com.khangle.myfitnessapp.ui.statistic.report

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.khangle.myfitnessapp.R
import com.khangle.myfitnessapp.ui.statistic.StatisticFragment


class StatReportFragment : Fragment() {
    private val viewmodel = (requireParentFragment() as StatisticFragment).statisticViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_stat_report, container, false)
    }

}