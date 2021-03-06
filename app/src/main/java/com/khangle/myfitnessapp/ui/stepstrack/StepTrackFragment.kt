package com.khangle.myfitnessapp.ui.stepstrack

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.khangle.myfitnessapp.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StepTrackFragment : Fragment() {
    private lateinit var adapter: StepFragmentPageAdapter
    private lateinit var pager: ViewPager2
    private val viewModel: StepTrackViewModel by viewModels()
    private lateinit var tabLayout: TabLayout
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel.getStepHistory()
        val view = inflater.inflate(R.layout.fragment_step_track, container, false)
        pager = view.findViewById(R.id.stepPager)
        tabLayout = view.findViewById(R.id.stepTablayout)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = StepFragmentPageAdapter(this, viewModel)
        pager.adapter = adapter
        TabLayoutMediator(tabLayout, pager) { tab, position ->
            if (position == 0) {
                tab.text = "Lịch sử"
            } else {
                tab.text = "Biểu đồ"
            }
        }.attach()
    }


}