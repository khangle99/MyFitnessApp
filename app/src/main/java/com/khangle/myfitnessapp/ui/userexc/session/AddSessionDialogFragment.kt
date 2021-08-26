package com.khangle.myfitnessapp.ui.userexc.session

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.DialogFragment
import com.google.android.material.chip.Chip
import com.khangle.myfitnessapp.R
import com.khangle.myfitnessapp.model.user.Session
import com.khangle.myfitnessapp.model.user.UserStat
import com.khangle.myfitnessapp.ui.statistic.StatisticViewModel
import com.khangle.myfitnessapp.ui.userexc.UserExcViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class AddSessionDialogFragment constructor(private val viewmodel: UserExcViewModel) :
    DialogFragment() {

    private lateinit var cancelChip: Chip
    private lateinit var addChip: Chip
    private lateinit var nameTV: EditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add_session_dialog, container, false)
        cancelChip = view.findViewById(R.id.cancelAddSessionBtn)
        addChip = view.findViewById(R.id.confirmAdd)
        nameTV = view.findViewById(R.id.nameTV)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        cancelChip.setOnClickListener {
            dismiss()
        }

        addChip.setOnClickListener {
            val name = nameTV.text.toString()
            if (!name.equals("")) {
                val session = Session("", name)
                viewmodel.addExcSession(session) {
                    dismiss()
                }
            } else {
                Toast.makeText(context, "Name cant be empty", Toast.LENGTH_SHORT).show()
            }

        }
    }


}