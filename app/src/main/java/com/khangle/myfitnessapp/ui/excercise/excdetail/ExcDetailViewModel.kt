package com.khangle.myfitnessapp.ui.excercise.excdetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.khangle.myfitnessapp.data.MyFitnessAppRepository
import com.khangle.myfitnessapp.model.AppBodyStat
import com.khangle.myfitnessapp.model.BodyStat
import com.khangle.myfitnessapp.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExcDetailViewModel @Inject constructor(private val repository: MyFitnessAppRepository): BaseViewModel() {
    private var _appBodyStatList =  MutableLiveData<List<AppBodyStat>>()
    val appBodyStatList: LiveData<List<AppBodyStat>> = _appBodyStatList
    fun getBodyStatList() {
        viewModelScope.launch(Dispatchers.IO) {
            handleResponse {
                _appBodyStatList.postValue(repository.getAppBodyStat())
            }
        }
    }
}