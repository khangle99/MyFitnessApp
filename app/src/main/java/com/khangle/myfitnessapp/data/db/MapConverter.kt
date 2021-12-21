package com.khangle.myfitnessapp.data.db

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class MapConverter {
    @TypeConverter
    fun fromString(value: String): Map<String, String> {
        val mapType = object : TypeToken<Map<String, String>>() {}.type
        return Gson().fromJson(value, mapType)
    }

    @TypeConverter
    fun fromStringMap(map: Map<String, String>): String {
        val gson = Gson()
        return gson.toJson(map)
    }
}

class LevelMapConverter {
    @TypeConverter
    fun fromString(value: String): Map<String, Array<Int>> {
        val mapType = object : TypeToken<Map<String, Array<Int>>>() {}.type
        return Gson().fromJson(value, mapType)
    }

    @TypeConverter
    fun fromStringMap(map: Map<String, Array<Int>>): String {
        val gson = Gson()
        return gson.toJson(map)
    }
}