package com.khangle.myfitnessapp.model

import android.os.Parcel
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class BodyStat(val id: String, var value: String, var dateString: String,var statId: String): Parcelable