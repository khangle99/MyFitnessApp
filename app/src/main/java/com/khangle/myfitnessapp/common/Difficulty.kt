package com.khangle.myfitnessapp.common

enum class Difficulty(val raw: Int) {
    Beginner(0),
    Intermediate(1),
    Expert(2),
    Unknow(raw = 3);

    companion object {
        fun fromInt(value: Int): Difficulty {
            values().forEach {
                if (it.raw == value) return it
            }
            return Unknow
        }
    }
}