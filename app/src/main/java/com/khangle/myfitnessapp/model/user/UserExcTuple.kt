package com.khangle.myfitnessapp.model.user

import android.os.Parcel
import android.os.Parcelable
import com.khangle.myfitnessapp.model.Excercise

class UserExcTuple(
    val timeInfo: UserExcercise?,
    val excercise: Excercise?,
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readParcelable(UserExcercise::class.java.classLoader),
        parcel.readParcelable(Excercise::class.java.classLoader)) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(timeInfo, flags)
        parcel.writeParcelable(excercise, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<UserExcTuple> {
        override fun createFromParcel(parcel: Parcel): UserExcTuple {
            return UserExcTuple(parcel)
        }

        override fun newArray(size: Int): Array<UserExcTuple?> {
            return arrayOfNulls(size)
        }
    }
}