package com.khangle.myfitnessapp.ui.statistic.history

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.khangle.myfitnessapp.R
import com.khangle.myfitnessapp.common.SwipeToDeleteCallback
import com.khangle.myfitnessapp.ui.statistic.StatisticFragment
import com.khangle.myfitnessapp.ui.statistic.StatisticViewModel


class StatHistoryFragment(private val viewModel: StatisticViewModel) : Fragment() {
    private lateinit var addStatButton: FloatingActionButton
    private lateinit var statHistoryListAdapter: StatHistoryListAdapter
    private lateinit var historyRecyclerView: RecyclerView
    lateinit var progressBar: ProgressBar
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_stat_history, container, false)
        addStatButton = view.findViewById(R.id.addStatFAB)
        historyRecyclerView = view.findViewById(R.id.statHistoryList)
        progressBar = view.findViewById(R.id.statHistoryProgress)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupSwipeToDelete()
        statHistoryListAdapter = StatHistoryListAdapter()
        historyRecyclerView.adapter = statHistoryListAdapter
        historyRecyclerView.layoutManager = LinearLayoutManager(context)

        viewModel.statHistoryList.observe(viewLifecycleOwner) {
            statHistoryListAdapter.submitList(it)
            progressBar.visibility = View.INVISIBLE
        }

        addStatButton.setOnClickListener {
            AddStatDialogFragment(viewModel).show(childFragmentManager, "AddStat")
        }
    }

    private fun setupSwipeToDelete() {
        val callback = object : SwipeToDeleteCallback(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val item = statHistoryListAdapter.currentList[position]
                viewModel.removeStat(item) {
                    Toast.makeText(context, "Deleted Session", Toast.LENGTH_SHORT).show()
                }
            }
        }
        val itemTouchHelper = ItemTouchHelper(callback)
        itemTouchHelper.attachToRecyclerView(historyRecyclerView)
    }

}