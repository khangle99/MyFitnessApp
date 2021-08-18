package com.khangle.myfitnessapp.common

import java.text.SimpleDateFormat
import java.util.*


val dateFormat = SimpleDateFormat("dd/MM/yyyy")
fun String.toFormatDate(): Date? {
    return dateFormat.parse(this)
}