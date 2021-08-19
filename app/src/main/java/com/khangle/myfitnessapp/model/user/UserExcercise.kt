package com.khangle.myfitnessapp.model.user

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class UserExcercise(
    @PrimaryKey var id: String,
    var noGap: Int,
    var noSec: Int,
    var noTurn: Int,
    var sessionId: String,
    var categoryId: String,
    var excId: String
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString()?: "",
        parcel.readString()?: "",
        parcel.readString() ?: "") {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeInt(noGap)
        parcel.writeInt(noSec)
        parcel.writeInt(noTurn)
        parcel.writeString(sessionId)
        parcel.writeString(categoryId)
        parcel.writeString(excId)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<UserExcercise> {
        override fun createFromParcel(parcel: Parcel): UserExcercise {
            return UserExcercise(parcel)
        }

        override fun newArray(size: Int): Array<UserExcercise?> {
            return arrayOfNulls(size)
        }
    }
}