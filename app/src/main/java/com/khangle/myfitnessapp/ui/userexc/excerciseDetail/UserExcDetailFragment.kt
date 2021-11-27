package com.khangle.myfitnessapp.ui.userexc.excerciseDetail

import android.graphics.Color
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.*
import android.widget.*
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import com.google.android.material.chip.Chip
import com.khangle.myfitnessapp.R
import com.khangle.myfitnessapp.common.UseState
import com.khangle.myfitnessapp.extension.commitAnimate
import com.khangle.myfitnessapp.extension.setReadOnly
import com.khangle.myfitnessapp.model.user.PlanDay
import com.khangle.myfitnessapp.model.user.UserExcTuple
import com.khangle.myfitnessapp.model.user.UserExcercise
import com.khangle.myfitnessapp.ui.base.BaseComposableFragment
import com.khangle.myfitnessapp.ui.excercise.excdetail.ExcDetailFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserExcDetailFragment: BaseComposableFragment() {

    private val viewModel: UserExcDetailViewModel by viewModels()

    private lateinit var excNameTV: TextView
    private lateinit var daySpinner: Spinner
    private lateinit var viewDetail: Chip
    private var selectedDay: String = "Monday"
    private lateinit var planDay: PlanDay
    private val dayList = listOf("Monday","Tuesday","Wednesday","Thursday","Friday","Saturday","Sunday")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        val view = inflater.inflate(R.layout.fragment_user_exc_detail, container, false)
        excNameTV = view.findViewById(R.id.excName)
        viewDetail = view.findViewById(R.id.viewExcDetailBtn)
        daySpinner = view.findViewById(R.id.dayinWeekSpinner)
        setupDaySpinner()
        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val stateRaw = requireArguments().getInt("state") // default la 0 ?
        changeState(UseState.values().firstOrNull { it.raw == stateRaw } ?: UseState.VIEW)

        if (state == UseState.ADD) {
            excNameTV.text = requireArguments().getString("excName")
        } else {
            planDay = requireArguments().getParcelable("planDay")!!
            loadPlanDay(planDay)
            if (requireArguments().getBoolean("isDeleted", false)) {
                viewDetail.visibility = View.INVISIBLE
                val spannableString = SpannableString("This Excercise has been deleted by admin! Please manually remove this reference")
                spannableString.setSpan(
                    ForegroundColorSpan(Color.RED),
                    0, // start
                    spannableString.length , // end
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                excNameTV.text = spannableString
            }
            viewDetail.setOnClickListener {
                val frag = ExcDetailFragment()
                frag.arguments = bundleOf("excercise" to planDay.exc, "isViewOnly" to true)
                parentFragmentManager.commitAnimate {
                    addToBackStack(null)
                    replace(R.id.userexcContainer, frag)
                }
            }
        }


    }

    private fun loadPlanDay(planDay: PlanDay) {
        excNameTV.setText(planDay.exc?.name)
        daySpinner.setSelection(planDay.day.toInt())
    }

    private fun setupDaySpinner() {
        daySpinner.adapter = ArrayAdapter(requireActivity(),R.layout.item_spinner, dayList)
        daySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long,
            ) {
                selectedDay = position.toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) { }
        }
    }

    override fun onAdded() {
        if (!validateInput()) return

        val categoryId = requireArguments().getString("categoryId")!!
        val excId = requireArguments().getString("excId")!!

        viewModel.updatePlanDay(categoryId,excId, selectedDay!!, "9") { message -> // 9 is create new day
            if (message.id != null) {
                Toast.makeText(
                    requireContext(),
                    "Thêm thành công với id: ${message.id}",
                    Toast.LENGTH_SHORT
                ).show()
                parentFragmentManager.popBackStack()
            } else {
                Toast.makeText(
                    requireContext(),
                    "Lỗi khi thêm error: ${message.error}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun onUpdated() {
        if (!validateInput()) return

        val excId = requireArguments().getString("excId")!!
        viewModel.updatePlanDay(planDay.categoryId,excId, selectedDay!!,planDay.day) { message ->
            if (message.id != null) {
                Toast.makeText(
                    requireContext(),
                    "Thêm thành công với id: ${message.id}",
                    Toast.LENGTH_SHORT
                ).show()
                parentFragmentManager.popBackStack()
            } else {
                Toast.makeText(
                    requireContext(),
                    "Lỗi khi thêm error: ${message.error}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun onDeleted() {
        viewModel.deletePlanDay(selectedDay!!) { message ->
            if (message.id != null) {
                Toast.makeText(
                    requireContext(),
                    "Delete thành công với id: ${message.id}",
                    Toast.LENGTH_SHORT
                ).show()
                parentFragmentManager.popBackStack()
            } else {
                Toast.makeText(
                    requireContext(),
                    "Lỗi khi delete error: ${message.error}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun getManageObjectName(): String {
        return "Excercise"
    }

    override fun invalidateView() {
        if (state == UseState.ADD) {
            viewDetail.visibility = View.INVISIBLE
        } else {
            viewDetail.visibility = View.VISIBLE
        }

        daySpinner.isEnabled = state == UseState.ADD || state == UseState.EDIT
    }

    private fun validateInput(): Boolean {
        return true
    }


}