package com.khangle.myfitnessapp.ui.nutrition.category

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.khangle.myfitnessapp.R
import com.khangle.myfitnessapp.extension.commitAnimate
import com.khangle.myfitnessapp.ui.excercise.exclist.ExcListFragment
import com.khangle.myfitnessapp.ui.nutrition.NutritionViewModel
import com.khangle.myfitnessapp.ui.nutrition.menulist.MenuListFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NutritionCategoryFragment : Fragment() {

    lateinit var recyclerView: RecyclerView
    lateinit var adapter: NutCategoryListAdapter
    lateinit var progressBar: ProgressBar
   private val viewModel: NutritionViewModel by activityViewModels()

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
        viewModel.nutCategory.observe(viewLifecycleOwner, {
            adapter.submitList(it)
            progressBar.visibility = View.INVISIBLE
        })
        viewModel.invalidateNutCategory()
        adapter = NutCategoryListAdapter {
            val frag = MenuListFragment()
            frag.arguments = bundleOf("category" to it)
            parentFragmentManager.commitAnimate {
                    replace(R.id.nutritionContainer, frag)
            }
        }
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)
    }

}