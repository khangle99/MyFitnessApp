package com.khangle.myfitnessapp.model

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class NutritionCategory(@PrimaryKey val id: String, var name: String, var photoUrl: String): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: ""
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(name)
        parcel.writeString(photoUrl)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<NutritionCategory> {
        override fun createFromParcel(parcel: Parcel): NutritionCategory {
            return NutritionCategory(parcel)
        }

        override fun newArray(size: Int): Array<NutritionCategory?> {
            return arrayOfNulls(size)
        }
    }
}