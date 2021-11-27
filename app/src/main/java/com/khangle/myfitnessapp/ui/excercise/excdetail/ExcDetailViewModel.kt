package com.khangle.myfitnessapp.ui.excercise.excdetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.khangle.myfitnessapp.data.MyFitnessAppRepository
import com.khangle.myfitnessapp.model.BodyStat
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExcDetailViewModel @Inject constructor(private val repository: MyFitnessAppRepository): ViewModel() {
//    private var _bodyStatList =  MutableLiveData<List<BodyStat>>()
//    val bodyStatList: LiveData<List<BodyStat>> = _bodyStatList
//    fun getBodyStatList() {
//        viewModelScope.launch(Dispatchers.IO) {
//            _bodyStatList.postValue(repository.getBodyStat())
//        }
//    }
}