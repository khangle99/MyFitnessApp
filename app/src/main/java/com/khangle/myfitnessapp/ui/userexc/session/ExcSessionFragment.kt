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
import com.khangle.myfitnessapp.ui.userexc.UserSessionViewModel
import com.khangle.myfitnessapp.ui.userexc.excList.UserExcListFragment


class ExcSessionFragment constructor(private val userExcerciseViewModel: UserSessionViewModel): Fragment() {

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
            val frag = UserExcListFragment()
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
            progressBar.visibility = View.INVISIBLE
            if(it.isNotEmpty()) {
                adapter.submitList(it)
            } else {
                Toast.makeText(context,"No Session added", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupSwipeToDelete() {
        val callback = object : SwipeToDeleteCallback(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val item = adapter.currentList[position]
                progressBar.visibility = View.VISIBLE
                sessionRecyclerView.isEnabled = false
               userExcerciseViewModel.removeExcSession(item.id) {
                   progressBar.visibility = View.INVISIBLE
                   if(it.id != null) {
                       sessionRecyclerView.isEnabled = true
                       Toast.makeText(context, "Deleted Session", Toast.LENGTH_SHORT).show()
                   } else {
                       Toast.makeText(context, "Cant deleted Session have excercise", Toast.LENGTH_SHORT).show()
                       adapter.notifyDataSetChanged()
                   }

               }
            }

        }
        val itemTouchHelper = ItemTouchHelper(callback)
        itemTouchHelper.attachToRecyclerView(sessionRecyclerView)
    }

}