package com.khangle.myfitnessapp.ui.userexc.excerciseDetail

import android.graphics.Color
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.*
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import com.google.android.material.chip.Chip
import com.khangle.myfitnessapp.R
import com.khangle.myfitnessapp.common.UseState
import com.khangle.myfitnessapp.extension.commitAnimate
import com.khangle.myfitnessapp.extension.setReadOnly
import com.khangle.myfitnessapp.model.user.UserExcTuple
import com.khangle.myfitnessapp.model.user.UserExcercise
import com.khangle.myfitnessapp.ui.base.BaseComposableFragment
import com.khangle.myfitnessapp.ui.excercise.excdetail.ExcDetailFragment
import com.khangle.myfitnessapp.ui.userexc.UserExcViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserExcDetailFragment: BaseComposableFragment() {

    private val viewModel: UserExcViewModel by viewModels()

    private lateinit var excNameTV: TextView
    private lateinit var noGapEditText: EditText
    private lateinit var noSecEditText: EditText
    private lateinit var noTurnEditText: EditText
    private lateinit var sessionSpinner: Spinner
    private lateinit var viewDetail: Chip
    private lateinit var sessionTitleTV: TextView

    private var selectedSessionId = ""
    private var userExcercise: UserExcercise? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        val view = inflater.inflate(R.layout.fragment_user_exc_detail, container, false)
        excNameTV = view.findViewById(R.id.excName)
        noGapEditText = view.findViewById(R.id.noGap)
        noSecEditText = view.findViewById(R.id.noSec)
        noTurnEditText = view.findViewById(R.id.noTurn)
        viewDetail = view.findViewById(R.id.viewExcDetailBtn)
        sessionSpinner = view.findViewById(R.id.sessionSpinner)
        sessionTitleTV = view.findViewById(R.id.sessionTitle)
        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val stateRaw = requireArguments().getInt("state") // default la 0 ?
        changeState(UseState.values().firstOrNull { it.raw == stateRaw } ?: UseState.VIEW)

        if (state == UseState.ADD) {
            excNameTV.text = requireArguments().getString("excName")
            loadSessionSpiner()
        } else {
            val tuple: UserExcTuple = requireArguments().getParcelable("tuple")!!
            loadTuple(tuple)
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
                frag.arguments = bundleOf("excercise" to tuple.excercise, "isViewOnly" to true)
                parentFragmentManager.commitAnimate {
                    addToBackStack(null)
                    replace(R.id.userexcContainer, frag)

                }
            }
        }


    }

    private fun loadSessionSpiner() {
        viewModel.excerciseSession.observe(viewLifecycleOwner) {
            val nameList = it.map { it.name }
            sessionSpinner.adapter = ArrayAdapter(requireContext(),R.layout.item_spinner, nameList)
        }
        sessionSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long,
            ) {
                selectedSessionId = viewModel.excerciseSession.value!![position].id
            }

            override fun onNothingSelected(parent: AdapterView<*>?) { }
        }
    }

    private fun loadTuple(tuple: UserExcTuple) {
        userExcercise = tuple.timeInfo
        excNameTV.text = tuple.excercise!!.name
        noGapEditText.setText(tuple.timeInfo!!.noGap.toString())
        noSecEditText.setText(tuple.timeInfo.noSec.toString())
        noTurnEditText.setText(tuple.timeInfo.noTurn.toString())
    }


    override fun onAdded() {
        if (!validateInput()) return
        val noGap = noGapEditText.text.toString()
        val noSec = noSecEditText.text.toString()
        val noTurn = noTurnEditText.text.toString()

        val categoryId = requireArguments().getString("categoryId")!!
        val excId = requireArguments().getString("excId")!!
        val userExcercise = UserExcercise("",noGap.toInt(), noSec.toInt(), noTurn.toInt(),selectedSessionId, categoryId, excId)
        viewModel.addUserExcercise(userExcercise, selectedSessionId) { message ->
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
        userExcercise!!.noTurn = noTurnEditText.text.toString().toInt()
        userExcercise!!.noSec = noSecEditText.text.toString().toInt()
        userExcercise!!.noGap = noGapEditText.text.toString().toInt()
        viewModel.updateUserExcercise(userExcercise!!) { message ->
            if (message.id != null) {
                Toast.makeText(
                    requireContext(),
                    "Update thành công với id: ${message.id}",
                    Toast.LENGTH_SHORT
                ).show()
                parentFragmentManager.popBackStack()
            } else {
                Toast.makeText(
                    requireContext(),
                    "Lỗi khi update error: ${message.error}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun onDeleted() {
        viewModel.deleteUserExcercise(userExcercise!!) { message ->
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
        when (state) {
            UseState.EDIT, UseState.ADD -> {
                noGapEditText.setReadOnly(false)
                noTurnEditText.setReadOnly(false)
                noSecEditText.setReadOnly(false)

            }
            else -> {
                noGapEditText.setReadOnly(true)
                noTurnEditText.setReadOnly(true)
                noSecEditText.setReadOnly(true)

            }
        }

        if (state == UseState.ADD) {
            sessionSpinner.visibility = View.VISIBLE
            sessionTitleTV.visibility = View.VISIBLE
            viewDetail.visibility = View.INVISIBLE
        } else {
            sessionSpinner.visibility = View.GONE
            sessionTitleTV.visibility = View.GONE
            viewDetail.visibility = View.VISIBLE
        }
    }

    private fun validateInput(): Boolean { // false khi fail
        if (noGapEditText.toString().trim { it <= ' ' }.length == 0) {
            noGapEditText.setError("Không được để trống Gap")
            return false
        }
        if (noSecEditText.getText().toString().trim { it <= ' ' }.length == 0) {
            noSecEditText.setError("Không được để trống Sec")
            return false
        }
        if (noTurnEditText.getText().toString().trim { it <= ' ' }.length == 0) {
            noTurnEditText.setError("Không được để trống no Turn")
            return false
        }
        return true
    }


}