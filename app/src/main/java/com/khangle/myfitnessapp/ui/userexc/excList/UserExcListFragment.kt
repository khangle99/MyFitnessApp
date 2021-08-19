package com.khangle.myfitnessapp.ui.userexc.excList

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.khangle.myfitnessapp.R
import com.khangle.myfitnessapp.extension.commitAnimate
import com.khangle.myfitnessapp.model.user.Session
import com.khangle.myfitnessapp.ui.userexc.UserExcViewModel
import com.khangle.myfitnessapp.ui.userexc.excerciseDetail.UserExcDetailFragment


class UserExcListFragment constructor(private val viewModel: UserExcViewModel): Fragment() {
    private lateinit var nameTV: TextView
    private lateinit var timeTV: TextView
    private lateinit var excListRecyclerView: RecyclerView
    private lateinit var userListAdapter: UserExcListAdapter
    private lateinit var startWorkoutBtn: Chip

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_user_exc_list, container, false)
        nameTV = view.findViewById(R.id.sessionNameTv)
        timeTV = view.findViewById(R.id.sessionTimeTV)
        excListRecyclerView = view.findViewById(R.id.userexcList)
        startWorkoutBtn = view.findViewById(R.id.startWorkoutBtn)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val session: Session = requireArguments().getParcelable("session")!!

        startWorkoutBtn.setOnClickListener {
            // pass data to new activity to count down
        }


        userListAdapter = UserExcListAdapter {
            val frag = UserExcDetailFragment()
            frag.arguments = bundleOf("tuple" to it)
            parentFragmentManager.commitAnimate {
                addToBackStack(null)
                replace(R.id.userexcContainer, frag)

            }
        }
        excListRecyclerView.adapter = userListAdapter
        excListRecyclerView.layoutManager = LinearLayoutManager(context)

        viewModel.excerciseTuple.observe(viewLifecycleOwner) {
            userListAdapter.submitList(it)
        }
        viewModel.invalidateUserExcercise(session.id)
         nameTV.text = session.name
    }

}