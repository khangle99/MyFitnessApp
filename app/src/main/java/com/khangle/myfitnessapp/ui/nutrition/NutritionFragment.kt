package com.khangle.myfitnessapp.ui.nutrition

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.add
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.khangle.myfitnessapp.R
import com.khangle.myfitnessapp.databinding.FragmentNutritionBinding
import com.khangle.myfitnessapp.extension.commitAnimate
import com.khangle.myfitnessapp.ui.excercise.category.ExcerciseCategoryFragment
import com.khangle.myfitnessapp.ui.nutrition.category.NutritionCategoryFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NutritionFragment : Fragment() {

    private var _binding: FragmentNutritionBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNutritionBinding.inflate(inflater, container, false)
        val root: View = binding.root
        childFragmentManager.commitAnimate {
            setReorderingAllowed(true)
            add<NutritionCategoryFragment>(R.id.nutritionContainer)
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}