package com.khangle.myfitnessapp.ui.base

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment

open class BaseDialogFragment: DialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val dialogWindow = dialog?.window
        if(dialogWindow !=null) {
            dialogWindow.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialogWindow.requestFeature(Window.FEATURE_NO_TITLE)
        }
        return super.onCreateView(inflater, container, savedInstanceState)

    }
}