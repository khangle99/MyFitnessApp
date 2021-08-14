package com.khangle.myfitnessapp.extension

import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.khangle.myfitnessapp.R

inline fun FragmentManager.commitAnimate(
    allowStateLoss: Boolean = false,
    body: FragmentTransaction.() -> Unit
) {
    val transaction = beginTransaction()
    transaction.setCustomAnimations(R.anim.slide_in,R.anim.fade_out, R.anim.fade_in, R.anim.slide_out)

    transaction.body()
    transaction.addToBackStack(null)
    if (allowStateLoss) {
        transaction.commitAllowingStateLoss()
    } else {
        transaction.commit()
    }
}