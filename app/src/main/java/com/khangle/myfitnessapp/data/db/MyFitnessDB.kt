package com.khangle.myfitnessapp.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.khangle.myfitnessapp.model.Excercise
import com.khangle.myfitnessapp.model.ExcerciseCategory
import com.khangle.myfitnessapp.model.Menu
import com.khangle.myfitnessapp.model.NutritionCategory
import com.khangle.myfitnessapp.model.user.Session
import com.khangle.myfitnessapp.model.user.UserExcercise
import com.khangle.myfitnessapp.model.user.UserStat
import com.khangle.myfitnessapp.model.user.UserStep


@Database(
    entities = arrayOf(
        Session::class,
        UserExcercise::class,
        UserStat::class,
        UserStep::class,
        NutritionCategory::class,
        Menu::class,
        ExcerciseCategory::class,
        Excercise::class
    ), version = 1
)
abstract class MyFitnessDB : RoomDatabase() {
    abstract fun myFitnessDao(): MyFitnessDao
}