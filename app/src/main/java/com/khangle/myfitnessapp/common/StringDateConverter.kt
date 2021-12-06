package com.khangle.myfitnessapp.common

import java.text.SimpleDateFormat
import java.util.*


val dateFormat = SimpleDateFormat("dd/MM/yyyy")
fun String.toFormatDate(): Date? {
    return dateFormat.parse(this)
}

fun String.isSameMonth(compareString: String): Boolean {

    val calendar = Calendar.getInstance()
    calendar.time = this.toFormatDate()  ?: return false

    val cmpCalendar = Calendar.getInstance()
    cmpCalendar.time = compareString.toFormatDate()  ?: return false

    return calendar.get(Calendar.MONTH) == cmpCalendar.get(Calendar.MONTH)
}