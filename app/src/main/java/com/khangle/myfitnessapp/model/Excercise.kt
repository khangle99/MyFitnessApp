package com.khangle.myfitnessapp.model

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class Excercise(
    @PrimaryKey val id: String = "",
    var name: String,
    var difficulty: String,
    var equipment: String,
    var tutorial: String,
    val picSteps: List<String>,
    var catId: String
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.createStringArrayList() ?: listOf(),
        parcel.readString() ?: ""
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(name)
        parcel.writeString(difficulty)
        parcel.writeString(equipment)
        parcel.writeString(tutorial)
        parcel.writeStringList(picSteps)
        parcel.writeString(catId)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Excercise> {
        override fun createFromParcel(parcel: Parcel): Excercise {
            return Excercise(parcel)
        }

        override fun newArray(size: Int): Array<Excercise?> {
            return arrayOfNulls(size)
        }
    }
}