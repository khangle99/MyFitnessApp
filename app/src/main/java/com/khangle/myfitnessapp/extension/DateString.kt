package com.khangle.myfitnessapp.extension



import java.text.SimpleDateFormat
import java.util.*

fun String.toDate(): Date {
    return SimpleDateFormat("dd/MM/yyyy").parse(this) ?: Date()
}
fun Date.toFormatString(): String {
    return SimpleDateFormat("dd/MM/yyyy").format(this)
}