package com.khangle.myfitnessapp.ui.excercise

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.add
import androidx.fragment.app.commit
import com.khangle.myfitnessapp.R
import com.khangle.myfitnessapp.databinding.FragmentExcerciseBinding
import com.khangle.myfitnessapp.ui.excercise.category.ExcerciseCategoryFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ExcerciseFragment : Fragment() {

    private var _binding: FragmentExcerciseBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentExcerciseBinding.inflate(inflater, container, false)
        val root: View = binding.root
        childFragmentManager.commit {
            setReorderingAllowed(true)
            add<ExcerciseCategoryFragment>(R.id.excerciseContainer)
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}