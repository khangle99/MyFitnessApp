package com.khangle.myfitnessapp.model.user

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class ExcLog(val id: String, val categoryId: String, val excId: String, var dateInMonth: String):
    Parcelable