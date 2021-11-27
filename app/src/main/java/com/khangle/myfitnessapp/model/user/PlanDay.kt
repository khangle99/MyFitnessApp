package com.khangle.myfitnessapp.model.user

import android.os.Parcelable
import com.khangle.myfitnessapp.model.Excercise
import kotlinx.android.parcel.Parcelize

@Parcelize
class PlanDay(val id: String, val categoryId: String, val excId: String, var day: String, var exc: Excercise? = null):
    Parcelable