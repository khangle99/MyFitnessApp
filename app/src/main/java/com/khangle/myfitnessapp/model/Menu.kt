package com.khangle.myfitnessapp.model

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class Menu(
    @PrimaryKey val id: String,
    var name: String,
    var breakfast: String,
    var lunch: String,
    var dinner: String,
    var snack: String,
    var other: String,
    val picUrls: List<String>,
    var catId: String
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
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
        parcel.writeString(breakfast)
        parcel.writeString(lunch)
        parcel.writeString(dinner)
        parcel.writeString(snack)
        parcel.writeString(other)
        parcel.writeStringList(picUrls)
        parcel.writeString(catId)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Menu> {
        override fun createFromParcel(parcel: Parcel): Menu {
            return Menu(parcel)
        }

        override fun newArray(size: Int): Array<Menu?> {
            return arrayOfNulls(size)
        }
    }
}