package com.khangle.myfitnessapp.ui.statistic.otherstat

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.*
import androidx.fragment.app.DialogFragment
import com.google.android.material.chip.Chip
import com.khangle.myfitnessapp.R
import com.khangle.myfitnessapp.model.BodyStat
import com.khangle.myfitnessapp.model.user.UserStat
import com.khangle.myfitnessapp.ui.base.BaseDialogFragment
import com.khangle.myfitnessapp.ui.statistic.StatisticViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*


class AddBodyStatDialogFragment constructor(private val viewmodel: OtherStatViewModel): BaseDialogFragment() {

    private lateinit var cancelChip: Chip
    private lateinit var addChip: Chip
    private lateinit var unitTextView: TextView
    private lateinit var valueEditText: EditText
    private lateinit var statSpinner: Spinner

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        val view = inflater.inflate(R.layout.fragment_add_body_stat_dialog, container, false)
        cancelChip = view.findViewById(R.id.cancelAddStatBtn)
        addChip = view.findViewById(R.id.addStatBtn)
        unitTextView = view.findViewById(R.id.unitTextview)
        valueEditText = view.findViewById(R.id.statValue)
        statSpinner = view.findViewById(R.id.statSpinner)
        setupSpinner()
        return view
    }

    private fun setupSpinner() {
        statSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?,
                                        view: View?,
                                        position: Int,
                                        id: Long,) {
               viewmodel.appBodyStatList.value?.get(position)?.unit?.let {
                   unitTextView.setText(it)
               }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadSpinner()

        cancelChip.setOnClickListener {
            dismiss()
        }
        addChip.setOnClickListener {
            val stat = getStatFromInput()
            viewmodel.addOtherStat(stat) { message ->

                if (message.id != null) {
                    Toast.makeText(
                        requireContext(),
                        "Delete thành công với id: ${message.id}",
                        Toast.LENGTH_SHORT
                    ).show()
                    viewmodel.getStatHistory()

                } else {
                    Toast.makeText(
                        requireContext(),
                        "Lỗi khi delete error: ${message.error}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                dismiss()

            }
        }
    }

    private fun loadSpinner() {
        val statNameList = viewmodel.appBodyStatList.value?.map {
            it.name
        }
        statNameList?.let {
            val weighAdapter = ArrayAdapter(requireContext(),android.R.layout.simple_spinner_item, it)
            statSpinner.adapter = weighAdapter
        }

    }

    private fun getStatFromInput(): BodyStat {

        val id = viewmodel.appBodyStatList.value?.get(statSpinner.selectedItemPosition)?.id ?: ""
        val value = valueEditText.text.toString()
        return BodyStat("",value, composeDateString(), id)
    }

    private fun composeDateString(): String {
        val date = Calendar.getInstance().time
        val dateFormat = SimpleDateFormat("dd/MM/yyyy")
        val strDate: String = dateFormat.format(date)
        return strDate
    }

}