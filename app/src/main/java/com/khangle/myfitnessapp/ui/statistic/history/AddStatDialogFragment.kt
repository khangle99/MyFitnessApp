package com.khangle.myfitnessapp.ui.statistic.history

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.google.android.material.chip.Chip
import com.khangle.myfitnessapp.R
import com.khangle.myfitnessapp.model.user.UserStat
import com.khangle.myfitnessapp.ui.base.BaseDialogFragment
import com.khangle.myfitnessapp.ui.statistic.StatisticViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class AddStatDialogFragment constructor(private val viewmodel: StatisticViewModel): BaseDialogFragment() {

    private lateinit var cancelChip: Chip
    private lateinit var addChip: Chip
    private lateinit var datePicker: DatePicker
    private lateinit var weightSpinner: Spinner
    private lateinit var heightSpinner: Spinner

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        val view = inflater.inflate(R.layout.fragment_add_stat_dialog, container, false)
        cancelChip = view.findViewById(R.id.cancelAddStatBtn)
        addChip = view.findViewById(R.id.addStatBtn)
        datePicker = view.findViewById(R.id.statDatePicker)
        weightSpinner = view.findViewById(R.id.weightSpinner)
        heightSpinner = view.findViewById(R.id.heightSpinner)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadSpinner()
        datePicker.minDate = System.currentTimeMillis()
        cancelChip.setOnClickListener {
            dismiss()
        }
        addChip.setOnClickListener {
            val stat = getStatFromInput()
            viewmodel.addStat(stat) {
                dismiss()
            }
        }
    }

    private fun loadSpinner() {
        val weightList: List<Int> = (30..200).toList()
        val weighAdapter = ArrayAdapter(requireContext(),android.R.layout.simple_spinner_item, weightList)
        weightSpinner.adapter = weighAdapter

        val heightList: List<Int> = (100..200).toList()
        val heighAdapter = ArrayAdapter(requireContext(),android.R.layout.simple_spinner_item, heightList)
        heightSpinner.adapter = heighAdapter
    }

    private fun getStatFromInput(): UserStat {
        val weight = weightSpinner.selectedItem.toString().toInt()
        val height = heightSpinner.selectedItem.toString().toInt()
        return UserStat("",composeDateString(), weight, height)
    }

    private fun composeDateString(): String {
        return "${datePicker.dayOfMonth}/${datePicker.month + 1}/ ${datePicker.year}"
    }

}