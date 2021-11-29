package com.khangle.myfitnessapp.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class AppBodyStat(val id: String, var name: String, var dataType: String, var unit: String): Parcelable