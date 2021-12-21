package com.khangle.myfitnessapp.ui.userexc.workout

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.google.android.material.chip.Chip
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.khangle.myfitnessapp.R
import com.khangle.myfitnessapp.ui.base.BaseDialogFragment


class LevelPickFragment(private val onChooseLevel: (String) -> Unit) : BaseDialogFragment() {


    private lateinit var excerciseNameTV: TextView

    private lateinit var noGapTV: TextView
    private lateinit var noTurnTV: TextView
    private lateinit var noSecTV: TextView

    private lateinit var levelSpinner: Spinner

    private lateinit var cancelBtn: Chip
    private lateinit var okBtn: Chip

    private var selectLevelKey = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_level_pick, container, false)
        excerciseNameTV = view.findViewById(R.id.excerciseName)
        noGapTV = view.findViewById(R.id.noGap)
        noSecTV = view.findViewById(R.id.noSec)
        noTurnTV = view.findViewById(R.id.noTurn)

        levelSpinner = view.findViewById(R.id.levelSpinner)

        cancelBtn = view.findViewById(R.id.cancelbtn)
        cancelBtn.setOnClickListener {
            dismiss()
        }
        okBtn = view.findViewById(R.id.okBtn)
        okBtn.setOnClickListener {
            if (!selectLevelKey.equals("")) {
                onChooseLevel(selectLevelKey)
            } else {
                Toast.makeText(requireContext(), "Not select", Toast.LENGTH_SHORT).show()
            }
            dismiss()
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            excerciseNameTV.setText(it.getString("excName", "Exc Name"))

            val levelMap = it.getString("levelMapString", "")
            val mapType = object : TypeToken<Map<String, ArrayList<Int>>>() {}.type
            val map =  Gson().fromJson<Map<String, ArrayList<Int>>>(levelMap, mapType)

            setupLevelSpinner(map)

        }
    }

    private fun setupLevelSpinner(levelMap: Map<String, ArrayList<Int>>) {
        val array = levelMap.keys.toTypedArray()
        val adapter = ArrayAdapter(requireContext(),android.R.layout.simple_spinner_item, array)
        levelSpinner.adapter = adapter
        levelSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?,
                                        view: View?,
                                        position: Int,
                                        id: Long,) {
                 selectLevelKey = array[position]
                noTurnTV.setText(levelMap[selectLevelKey]?.get(0).toString() + " Lần")
                noSecTV.setText(levelMap[selectLevelKey]?.get(1).toString() + " Giây")
                noGapTV.setText(levelMap[selectLevelKey]?.get(2).toString() + " Giây nghỉ")

            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

        }
    }

}