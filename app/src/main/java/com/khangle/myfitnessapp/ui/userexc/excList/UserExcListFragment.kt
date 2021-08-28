package com.khangle.myfitnessapp.ui.userexc.excList

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.khangle.myfitnessapp.R
import com.khangle.myfitnessapp.extension.commitAnimate
import com.khangle.myfitnessapp.model.user.Session
import com.khangle.myfitnessapp.ui.userexc.UserExcViewModel
import com.khangle.myfitnessapp.ui.userexc.excerciseDetail.UserExcDetailFragment
import com.khangle.myfitnessapp.ui.userexc.workout.WorkoutActivity


class UserExcListFragment constructor(private val viewModel: UserExcViewModel): Fragment() {

    private lateinit var nameTV: EditText
    private lateinit var excListRecyclerView: RecyclerView
    private lateinit var userListAdapter: UserExcListAdapter
    private lateinit var startWorkoutBtn: Chip
    lateinit var progressBar: ProgressBar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_user_exc_list, container, false)
        nameTV = view.findViewById(R.id.sessionNameTv)
        excListRecyclerView = view.findViewById(R.id.userexcList)
        startWorkoutBtn = view.findViewById(R.id.startWorkoutBtn)
        progressBar = view.findViewById(R.id.excListProgress)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val session: Session = requireArguments().getParcelable("session")!!

        startWorkoutBtn.setOnClickListener {
            // pass data to new activity to count down
            val list = viewModel.excerciseTuple.value
            val intent = Intent(requireContext(), WorkoutActivity::class.java)
            intent.putExtras(bundleOf("tupleList" to list))
            startActivity(intent)
        }

        userListAdapter = UserExcListAdapter {
            val frag = UserExcDetailFragment()
            if (it.excercise?.name == null) {
                frag.arguments = bundleOf("tuple" to it, "isDeleted" to true)
            } else {
                frag.arguments = bundleOf("tuple" to it)
            }

            parentFragmentManager.commitAnimate {
                addToBackStack(null)
                replace(R.id.userexcContainer, frag)

            }
        }
        excListRecyclerView.adapter = userListAdapter
        excListRecyclerView.layoutManager = LinearLayoutManager(context)

        viewModel.excerciseTuple.observe(viewLifecycleOwner) {
            userListAdapter.submitList(it)
            progressBar.visibility = View.INVISIBLE
           if (it.isEmpty()) {
               startWorkoutBtn.visibility = View.INVISIBLE
           } else {
               startWorkoutBtn.visibility = View.VISIBLE
           }
        }

        viewModel.invalidateUserExcercise(session.id)
         nameTV.setText(session.name)
        nameTV.addTextChangedListener {
            session.name = it.toString()
            viewModel.updateSession(session) {

            }
        }
    }

}