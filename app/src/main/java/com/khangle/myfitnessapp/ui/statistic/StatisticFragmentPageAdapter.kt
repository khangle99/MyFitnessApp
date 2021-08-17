package com.khangle.myfitnessapp.ui.statistic

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.khangle.myfitnessapp.ui.statistic.history.StatHistoryFragment
import com.khangle.myfitnessapp.ui.statistic.report.StatReportFragment


class StatisticFragmentPageAdapter(fragment: Fragment, private val viewmodel: StatisticViewModel) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        if (position == 0) {
            return StatHistoryFragment(viewmodel)
        } else {
            return StatReportFragment(viewmodel)
        }
    }
}