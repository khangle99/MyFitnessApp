package com.khangle.myfitnessapp.common

object BMICalculator {

    fun bmiFrom(weight: Int, height: Int): Double{
        val heightInMeter = height/100.0
        val bmi = (weight * 1.0)/(heightInMeter*heightInMeter)
        return  Math.round(bmi * 10.0) / 10.0
    }

    fun getTypeFrom(bmi: Double):  BMIType {
        return BMIType.healthy
    }
}

enum class BMIType {
    underweight, healthy, overweight, obese
}