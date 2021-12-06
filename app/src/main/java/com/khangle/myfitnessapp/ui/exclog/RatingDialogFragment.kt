package com.khangle.myfitnessapp.ui.exclog

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
import com.khangle.myfitnessapp.model.user.UserStat
import com.khangle.myfitnessapp.ui.base.BaseDialogFragment
import com.khangle.myfitnessapp.ui.statistic.StatisticViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class RatingDialogFragment constructor(private val message: String): BaseDialogFragment() {

    private lateinit var cancelChip: Chip
    private lateinit var addChip: Chip
    private lateinit var ratingTV: TextView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        val view = inflater.inflate(R.layout.fragment_rating_dialog, container, false)
        cancelChip = view.findViewById(R.id.cancelRatingBtn)
        addChip = view.findViewById(R.id.addRatingBtn)
        ratingTV = view.findViewById(R.id.ratingReport)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ratingTV.setText(message)

        cancelChip.setOnClickListener {
            dismiss()
        }
        addChip.setOnClickListener {
           dismiss()
        }
    }


}