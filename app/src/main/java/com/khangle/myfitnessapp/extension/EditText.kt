package com.khangle.myfitnessapp.extension

import android.widget.EditText


fun EditText.setReadOnly(value: Boolean) {
    isFocusable = !value
    isFocusableInTouchMode = !value
    this.setCursorVisible(!value)
    this.isEnabled = !value
    // this.setBackgroundColor(if(value) Color.TRANSPARENT else Color.BLUE);
}