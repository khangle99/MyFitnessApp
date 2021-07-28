package com.khangle.myfitnessapp.model.user

import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity
class UserExcercise(@PrimaryKey val id: String, var name: String, var autoplay: Boolean, var noGap: Int, var noSec: Int, var noTurn: Int,var sessionId: String)