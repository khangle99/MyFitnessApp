package com.khangle.myfitnessapp.ui.stepstrack

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.khangle.myfitnessapp.ui.stepstrack.report.StepReportFragment
import com.khangle.myfitnessapp.ui.stepstrack.stephistory.StepHistoryFragment


class StepFragmentPageAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        if (position == 0) {
            return StepHistoryFragment()
        } else {
            return StepReportFragment()
        }
    }
}