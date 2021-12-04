package com.khangle.myfitnessadmin.suggestpack.planlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.khangle.myfitnessapp.data.MyFitnessAppRepository
import com.khangle.myfitnessapp.model.Plan
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlanListViewModel @Inject constructor(private val repository: MyFitnessAppRepository): ViewModel() {
    private var _planList =  MutableLiveData<List<Plan>>()
    val planList: LiveData<List<Plan>> = _planList
    fun getPlanList() {
        viewModelScope.launch(Dispatchers.IO) {
            _planList.postValue(repository.loadSuggestPlan())
        }
    }
}