package com.khangle.myfitnessadmin.suggestpack.plandetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.Gson
import com.khangle.myfitnessapp.data.MyFitnessAppRepository
import com.khangle.myfitnessapp.data.network.ResponseMessage
import com.khangle.myfitnessapp.model.user.PlanDay
import com.khangle.myfitnessapp.model.user.UserStat
import com.khangle.myfitnessapp.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import javax.inject.Inject

@HiltViewModel
class PlanDetailViewModel @Inject constructor(private val repository: MyFitnessAppRepository): BaseViewModel() {
    private val uid = FirebaseAuth.getInstance().uid!!

    private var _dayList = MutableLiveData<List<PlanDay>>()
    val dayList: LiveData<List<PlanDay>> = _dayList
    fun loadList(sugId: String) {
        viewModelScope.launch(Dispatchers.IO) {
           handleResponse {
               val list = repository.loadPackageDayList(sugId).map {
                   async {
                       it.exc = repository.getExcercise(it.categoryId, it.excId)
                       val ensure =  repository.getStatEnsureList(it.excId,it.categoryId)
                       it.exc!!.achieveEnsure = Gson().toJson(ensure)
                       it.day = it.id // do cung value
                       it
                   }
               }.awaitAll()
               _dayList.postValue(list)
           }
        }
    }

    fun applyPackage(sugId: String, handle: (ResponseMessage) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
           handleResponse(handle) {
               val res = repository.applyPackage(sugId, uid)
               withContext(Dispatchers.Main) {
                   handle(res)
               }
           }
        }
    }

}