package com.khangle.myfitnessapp.ui.userexc.runtrack

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.khangle.myfitnessapp.data.MyFitnessAppRepository
import com.khangle.myfitnessapp.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RunTrackViewModel @Inject constructor(private val repository: MyFitnessAppRepository): BaseViewModel() {
    private val uid = FirebaseAuth.getInstance().uid!!
    var currentWeight = MutableLiveData<Int>()

    fun fetchCurrentWeight() {
        viewModelScope.launch(Dispatchers.IO) {
            handleResponse {
                repository.fetchStatisticList(uid).firstOrNull()?.let {
                    currentWeight.postValue(it.weight)
                }
            }

        }
    }
}