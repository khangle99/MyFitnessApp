package com.khangle.myfitnessapp.ui.excercise

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.khangle.myfitnessapp.databinding.FragmentExcerciseBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ExcerciseFragment : Fragment() {

    private val excerciseViewModel: ExcerciseViewModel by viewModels()
    private var _binding: FragmentExcerciseBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentExcerciseBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textGallery
        excerciseViewModel.text.observe(viewLifecycleOwner, {
            textView.text = it
        })
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}