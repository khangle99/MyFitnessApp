package com.khangle.myfitnessapp.ui.statistic.otherstat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.khangle.myfitnessapp.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OtherStatActivity : AppCompatActivity() {

    private val viewmodel: OtherStatViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_other_stat)
    }
}