package com.khangle.myfitnessapp.ui.exclog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.add
import com.khangle.myfitnessapp.R
import com.khangle.myfitnessapp.databinding.FragmentNutritionBinding
import com.khangle.myfitnessapp.extension.commitAnimate
import com.khangle.myfitnessapp.ui.exclog.calendar.NutritionCategoryFragment
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