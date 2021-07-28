package com.khangle.myfitnessapp.model.user

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class Session(@PrimaryKey val id: String, var name: String, var time: String)