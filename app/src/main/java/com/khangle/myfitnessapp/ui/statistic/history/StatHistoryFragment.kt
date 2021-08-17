package com.khangle.myfitnessapp.ui.statistic.history

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.khangle.myfitnessapp.R
import com.khangle.myfitnessapp.ui.statistic.StatisticFragment
import com.khangle.myfitnessapp.ui.statistic.StatisticViewModel


class StatHistoryFragment(private val viewModel: StatisticViewModel) : Fragment() {
    private lateinit var addStatButton: FloatingActionButton
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_stat_history, container, false)
        addStatButton = view.findViewById(R.id.addStatFAB)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addStatButton.setOnClickListener {
            AddStatDialogFragment(viewModel).show(childFragmentManager, "AddStat")
        }
    }

}