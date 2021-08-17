package com.khangle.myfitnessapp.ui.statistic

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.github.mikephil.charting.data.CombinedData
import com.google.android.material.tabs.TabLayoutMediator
import com.khangle.myfitnessapp.databinding.FragmentStatisticBinding
import dagger.hilt.android.AndroidEntryPoint
@AndroidEntryPoint
class StatisticFragment : Fragment() {

    private lateinit var pageAdapter: StatisticFragmentPageAdapter
    val statisticViewModel: StatisticViewModel by viewModels()
    private var _binding: FragmentStatisticBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        statisticViewModel.getStatHistory()
        _binding = FragmentStatisticBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        pageAdapter = StatisticFragmentPageAdapter(this, statisticViewModel)
        binding.statisPager.adapter = pageAdapter
        TabLayoutMediator(binding.statTablayout, binding.statisPager) { tab, position ->
            if (position == 0) {
                tab.text = "History"
            } else {
                tab.text = "Report"
            }
        }.attach()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}