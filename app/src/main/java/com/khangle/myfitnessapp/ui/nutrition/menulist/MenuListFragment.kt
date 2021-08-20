package com.khangle.myfitnessapp.ui.nutrition.menulist

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.khangle.myfitnessapp.R
import com.khangle.myfitnessapp.extension.commitAnimate
import com.khangle.myfitnessapp.model.ExcerciseCategory
import com.khangle.myfitnessapp.model.NutritionCategory
import com.khangle.myfitnessapp.ui.excercise.ExcerciseViewModel
import com.khangle.myfitnessapp.ui.excercise.excdetail.ExcDetailFragment
import com.khangle.myfitnessapp.ui.nutrition.NutritionViewModel
import com.khangle.myfitnessapp.ui.nutrition.menudetail.MenuDetailFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MenuListFragment : Fragment() {

    lateinit var excerciseList: RecyclerView
    lateinit var adapter: MenuListAdapter
    lateinit var progressBar: ProgressBar
    private val viewmodel: NutritionViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view= inflater.inflate(R.layout.fragment_menu_list, container, false)
        excerciseList = view.findViewById(R.id.menuList)
        progressBar = view.findViewById(R.id.menuProgress)
        adapter = MenuListAdapter {
            val frag = MenuDetailFragment()
            frag.arguments = bundleOf("menu" to it)
            parentFragmentManager.commitAnimate {
                replace(R.id.nutritionContainer, frag)
            }
        }
        excerciseList.adapter = adapter
        excerciseList.layoutManager = LinearLayoutManager(context)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val category: NutritionCategory? = arguments?.getParcelable("category")
        if (category != null) {
            loadListForCategory(category)
        }
        viewmodel.menuList.observe(viewLifecycleOwner) {
            adapter.submitList(it)
            progressBar.visibility = View.INVISIBLE
        }
    }

    private fun loadListForCategory(category: NutritionCategory) {
        viewmodel.invalidateMenu(category)
    }


}