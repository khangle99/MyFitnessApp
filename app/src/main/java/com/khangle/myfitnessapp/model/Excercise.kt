package com.khangle.myfitnessapp.model

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity
class Excercise(
    @PrimaryKey val id: String = "",
    var name: String,
    var difficulty: Int,
    var equipment: String,
    var noTurn: Int,
    var noSec: Int,
    var noGap: Int,
    var caloFactor: Float,
    var tutorial: List<String>,
    val picSteps: List<String>,
    var achieveEnsure: String, // json
    val addedCount: Int,
    var tutorialWithPic: Map<String, String>,
    var categoryId: String,
    var nutriFactor: String
): Parcelable

