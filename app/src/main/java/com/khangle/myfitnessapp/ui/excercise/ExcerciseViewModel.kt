package com.khangle.myfitnessapp.ui.excercise

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.khangle.myfitnessapp.data.MyFitnessAppRepository
import dagger.hilt.android.lifecycle.HiltViewModel

@HiltViewModel
class ExcerciseViewModel constructor(private val repository: MyFitnessAppRepository): ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is gallery Fragment"
    }
    val text: LiveData<String> = _text

    fun getExcerciseCategory() {

    }
}