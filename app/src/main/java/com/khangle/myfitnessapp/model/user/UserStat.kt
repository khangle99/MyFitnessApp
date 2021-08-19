package com.khangle.myfitnessapp.model.user

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class UserStat(@PrimaryKey var id: String, var dateString: String, var weight: Int, var height: Int)