package com.khangle.myfitnessapp.ui.userexc

import androidx.lifecycle.ViewModel
import com.khangle.myfitnessapp.data.MyFitnessAppRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UserExcViewModel @Inject constructor(private val repository: MyFitnessAppRepository): ViewModel() {

}