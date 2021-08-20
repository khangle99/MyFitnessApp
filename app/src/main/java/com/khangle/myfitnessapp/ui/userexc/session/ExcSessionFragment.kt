package com.khangle.myfitnessapp.ui.userexc.session

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.khangle.myfitnessapp.R
import com.khangle.myfitnessapp.common.SwipeToDeleteCallback
import com.khangle.myfitnessapp.extension.commitAnimate
import com.khangle.myfitnessapp.ui.userexc.UserExcViewModel
import com.khangle.myfitnessapp.ui.userexc.excList.UserExcListFragment


class ExcSessionFragment constructor(private val userExcerciseViewModel: UserExcViewModel): Fragment() {

    private lateinit var sessionRecyclerView: RecyclerView
    private lateinit var adapter: SessionListAdapter
    lateinit var progressBar: ProgressBar
    private lateinit var addSessionBtn: ExtendedFloatingActionButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_exc_session, container, false)
        sessionRecyclerView = view.findViewById(R.id.sessionRecycleview)
        addSessionBtn = view.findViewById(R.id.addSession)
        progressBar = view.findViewById(R.id.sessionProgress)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupSwipeToDelete()
        adapter = SessionListAdapter {
            val frag = UserExcListFragment(userExcerciseViewModel)
            frag.arguments = bundleOf("session" to it)
            parentFragmentManager.commitAnimate {
                addToBackStack(null)
                replace(R.id.userexcContainer, frag)

            }
        }

        sessionRecyclerView.adapter = adapter
        sessionRecyclerView.layoutManager = LinearLayoutManager(context)

        addSessionBtn.setOnClickListener {
            AddSessionDialogFragment(userExcerciseViewModel).show(childFragmentManager, "AddSession")
        }

        userExcerciseViewModel.excerciseSession.observe(viewLifecycleOwner) {
            adapter.submitList(it)
            progressBar.visibility = View.INVISIBLE
        }
    }

    private fun setupSwipeToDelete() {
        val callback = object : SwipeToDeleteCallback(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val item = adapter.currentList[position]
               userExcerciseViewModel.removeExcSession(item.id) {
                   Toast.makeText(context, "Deleted Session", Toast.LENGTH_SHORT).show()
               }
            }

        }
        val itemTouchHelper = ItemTouchHelper(callback)
        itemTouchHelper.attachToRecyclerView(sessionRecyclerView)
    }

}