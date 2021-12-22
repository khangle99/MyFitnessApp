package com.khangle.myfitnessapp.ui.userexc.planlist

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.google.gson.Gson
import com.khangle.myfitnessadmin.suggestpack.planlist.SuggestActivity
import com.khangle.myfitnessapp.R
import com.khangle.myfitnessapp.extension.commitAnimate
import com.khangle.myfitnessapp.ui.excercise.exclist.ExcListFragment
import com.khangle.myfitnessapp.ui.userexc.plandetail.UserExcDetailFragment
import com.khangle.myfitnessapp.ui.userexc.runtrack.RunTrackFragment
import com.khangle.myfitnessapp.ui.userexc.workout.LevelPickFragment
import com.khangle.myfitnessapp.ui.userexc.workout.WorkoutActivity
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class UserExcListFragment : Fragment() {

    private val viewModel: UserExcViewModel by viewModels()
    private lateinit var nameTV: TextView
    private lateinit var excListRecyclerView: RecyclerView
    private lateinit var userListAdapter: PlanDayAdapter
    private lateinit var startWorkoutBtn: Chip
    private lateinit var startRunBtn: Chip
    private lateinit var choosePackage: Chip
    lateinit var progressBar: ProgressBar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_user_exc_list, container, false)
        nameTV = view.findViewById(R.id.sessionNameTv)
        excListRecyclerView = view.findViewById(R.id.userexcList)
        startWorkoutBtn = view.findViewById(R.id.startWorkoutBtn)
        startRunBtn = view.findViewById(R.id.startRunBtn)
        progressBar = view.findViewById(R.id.excListProgress)
        choosePackage = view.findViewById(R.id.choosePackage)
        return view
    }

    override fun onResume() {
        super.onResume()

        viewModel.dayList.observe(viewLifecycleOwner) {
            progressBar.visibility = View.INVISIBLE
            if(it.isNotEmpty()) {
                userListAdapter.submitList(it)
                startWorkoutBtn.visibility = View.VISIBLE
                startRunBtn.visibility = View.VISIBLE
            } else {
                startWorkoutBtn.visibility = View.INVISIBLE
                startRunBtn.visibility = View.INVISIBLE
                Toast.makeText(context,"No Excercise added", Toast.LENGTH_SHORT).show()
            }
        }
        viewModel.loadList()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        startWorkoutBtn.setOnClickListener {
            // code lai ham bat dau tap luyen
            // pass data to new activity to count down

            // chi lay ra ngay tuong ung
            val calendar = Calendar.getInstance(TimeZone.getDefault())
            val dayInWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1 // do bat dau tu 1

            val selectDay = viewModel.dayList.value?.filter {
                it.day.equals(dayInWeek.toString())
            }

            if (selectDay == null || selectDay.size == 0) {
                Toast.makeText(context, "Chua co bai tap cho ngay hom nay", Toast.LENGTH_SHORT).show()
            } else {

                if (selectDay.first().exc?.id != null) {
                    val fragment = LevelPickFragment {
                         val intent = Intent(requireContext(), WorkoutActivity::class.java)
                         intent.putExtras(bundleOf("dayList" to selectDay, "selectLevel" to it))
                         startActivity(intent)
                    }
                    fragment.arguments = bundleOf(
                        "excName" to selectDay.first().exc!!.name,
                        "levelMapString" to Gson().toJson(selectDay.first().exc!!.levelJSON)
                    )
                    fragment.show(childFragmentManager,"selectLevel")

                } else {
                    Toast.makeText(context, "Bai tap ngay hom nay da bi xoa", Toast.LENGTH_SHORT).show()
                }



            }
        }

        startRunBtn.setOnClickListener {
            val frag = RunTrackFragment()
            frag.arguments = bundleOf()
            parentFragmentManager.commitAnimate {
                addToBackStack(null)
                replace(R.id.userexcContainer, frag)

            }
        }

        choosePackage.setOnClickListener {
            val intent = Intent(requireContext(), SuggestActivity::class.java)
            startActivity(intent)
        }

        userListAdapter = PlanDayAdapter {
//            // mo den man hinh bai tap (tai su dung)
            val frag = UserExcDetailFragment()
            val bundle = bundleOf("planDay" to it, "categoryId" to it.categoryId, "excId" to it.excId, "isDeleted" to true)

            if (it.exc?.name == null) {
                frag.arguments = bundleOf("planDay" to it, "categoryId" to it.categoryId, "excId" to it.excId, "isDeleted" to true)
            } else {
                frag.arguments = bundleOf("planDay" to it, "categoryId" to it.categoryId, "excId" to it.excId)
            }
            parentFragmentManager.commitAnimate {
                addToBackStack(null)
                replace(R.id.userexcContainer, frag)
            }


        }
        excListRecyclerView.adapter = userListAdapter
        excListRecyclerView.layoutManager = LinearLayoutManager(context)

      //  viewModel.loadList()

    }

}