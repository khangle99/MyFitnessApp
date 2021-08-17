package com.khangle.myfitnessapp.common

object BMICalculator {

    fun bmiFrom(weight: Int, height: Int): Double{
        val heightInMeter = height/100.0
        return (heightInMeter)/(weight*weight)
    }

    fun getTypeFrom(bmi: Double):  BMIType {
        return BMIType.healthy
    }
}

enum class BMIType {
    underweight, healthy, overweight, obese
}