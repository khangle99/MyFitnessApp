package com.khangle.myfitnessapp.model.user

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class UserStep(@PrimaryKey val id: String, val dateString: String, val steps: Int)