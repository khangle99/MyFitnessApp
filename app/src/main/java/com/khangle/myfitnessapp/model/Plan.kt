package com.khangle.myfitnessapp.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class Plan (val id: String, val description: String) : Parcelable