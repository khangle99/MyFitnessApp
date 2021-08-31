package com.khangle.myfitnessapp.ui.excercise.exclist

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.khangle.myfitnessapp.R
import com.khangle.myfitnessapp.extension.commitAnimate
import com.khangle.myfitnessapp.model.ExcerciseCategory
import com.khangle.myfitnessapp.ui.excercise.ExcerciseViewModel
import com.khangle.myfitnessapp.ui.excercise.excdetail.ExcDetailFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ExcListFragment : Fragment() {

    lateinit var excerciseList: RecyclerView
    lateinit var adapter: ExcerciseListAdapter
    lateinit var progressBar: ProgressBar
    private val viewmodel: ExcerciseListViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view= inflater.inflate(R.layout.fragment_exc_list, container, false)
        excerciseList = view.findViewById(R.id.excList)
        progressBar = view.findViewById(R.id.excProgress)
        adapter = ExcerciseListAdapter {
            val frag = ExcDetailFragment()
            frag.arguments = bundleOf("excercise" to it)
            parentFragmentManager.commitAnimate {
                replace(R.id.excerciseContainer, frag)
            }
        }
        excerciseList.adapter = adapter
        excerciseList.layoutManager = LinearLayoutManager(context)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val category: ExcerciseCategory? = arguments?.getParcelable("category")
        if (category != null) {
            loadListForCategory(category)
        }
        viewmodel.excerciseList.observe(viewLifecycleOwner) {
            progressBar.visibility = View.INVISIBLE
            if (it.isNotEmpty()) {
                adapter.submitList(it)
            } else {
                Toast.makeText(context, "No Excercise added", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loadListForCategory(category: ExcerciseCategory) {
        viewmodel.invalidateExcercise(category)
    }


}