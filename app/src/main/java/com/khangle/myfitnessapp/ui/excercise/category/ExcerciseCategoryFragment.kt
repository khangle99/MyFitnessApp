package com.khangle.myfitnessapp.ui.excercise.category

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.khangle.myfitnessapp.R
import com.khangle.myfitnessapp.ui.excercise.ExcerciseViewModel


class ExcerciseCategoryFragment : Fragment() {

    lateinit var recyclerView: RecyclerView
    lateinit var adapter: ExcCategoryListAdapter
   private val viewModel: ExcerciseViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_excercise_category, container, false)
        recyclerView = view.findViewById(R.id.excCategoryRecycleview)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.excerciseCategory.observe(viewLifecycleOwner, {
            adapter.submitList(it)
        })
        viewModel.invalidateExcCategory()
        adapter = ExcCategoryListAdapter {

        }
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)
    }

}