package com.khangle.myfitnessapp.ui.excercise.category

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commit
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.khangle.myfitnessapp.R
import com.khangle.myfitnessapp.extension.commitAnimate
import com.khangle.myfitnessapp.ui.excercise.ExcerciseViewModel
import com.khangle.myfitnessapp.ui.excercise.exclist.ExcListFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ExcerciseCategoryFragment : Fragment() {

    lateinit var recyclerView: RecyclerView
    lateinit var adapter: ExcCategoryListAdapter
    lateinit var progressBar: ProgressBar
   private val viewModel: ExcerciseViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_excercise_category, container, false)
        recyclerView = view.findViewById(R.id.excCategoryRecycleview)
        progressBar = view.findViewById(R.id.excCatProgress)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.excerciseCategory.observe(viewLifecycleOwner, {
            adapter.submitList(it)
            progressBar.visibility = View.INVISIBLE
        })
        viewModel.invalidateExcCategory()
        adapter = ExcCategoryListAdapter {
            val frag = ExcListFragment()
            frag.arguments = bundleOf("category" to it)
            parentFragmentManager.commitAnimate {
                     addToBackStack(null)
                    replace(R.id.excerciseContainer, frag)

            }
        }
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)

    }

}