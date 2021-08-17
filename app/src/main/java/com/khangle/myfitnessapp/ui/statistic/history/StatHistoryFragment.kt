package com.khangle.myfitnessapp.ui.statistic.history

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.khangle.myfitnessapp.R
import com.khangle.myfitnessapp.ui.statistic.StatisticFragment
import com.khangle.myfitnessapp.ui.statistic.StatisticViewModel


class StatHistoryFragment : Fragment() {
    private val viewmodel = (requireParentFragment() as StatisticFragment).statisticViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_stat_history, container, false)
    }

}