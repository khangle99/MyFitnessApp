package com.khangle.myfitnessapp.ui.userexc

import androidx.lifecycle.*
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.Gson
import com.khangle.myfitnessapp.data.MyFitnessAppRepository
import com.khangle.myfitnessapp.data.network.ResponseMessage
import com.khangle.myfitnessapp.model.user.PlanDay
import com.khangle.myfitnessapp.model.user.Session
import com.khangle.myfitnessapp.model.user.UserExcTuple
import com.khangle.myfitnessapp.model.user.UserExcercise
import com.khangle.myfitnessapp.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class UserSessionViewModel @Inject constructor(private val repository: MyFitnessAppRepository): BaseViewModel() {

    private val uid = FirebaseAuth.getInstance().uid!!

    // planday
//    private var _dayList = MutableLiveData<List<PlanDay>>()
//    val dayList: LiveData<List<PlanDay>> = _dayList
//    fun loadList(sugId: String) {
//        viewModelScope.launch(Dispatchers.IO) {
//            val list = repository.loadDayList(sugId).map {
//                async {
//                    it.exc = repository.getExcercise(it.categoryId, it.excId)
//                    val ensure =  repository.getStatEnsureList(it.excId,it.categoryId)
//                    it.exc!!.achieveEnsure = Gson().toJson(ensure)
//                    it.day = it.id // do cung value
//                    it
//                }
//            }.awaitAll()
//            _dayList.postValue(list)
//        }
//    }

//    private var _dayPlan = MutableLiveData<PlanDay>()
//    var dayPlan: LiveData<PlanDay> = _dayPlan
//    var notExistDayString = mutableListOf<String>()
//
//    private var _planList =  MutableLiveData<List<Plan>>()
//    val planList: LiveData<List<Plan>> = _planList
//    fun getPlanList() {
//        viewModelScope.launch(Dispatchers.IO) {
//            _planList.postValue(repository.getSuggestPlans())
//        }
//    }

    private var _dayList = MutableLiveData<List<PlanDay>>()
    val dayList: LiveData<List<PlanDay>> = _dayList
    fun getDayList(sugId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val list = repository.loadDayList(sugId).map {
                async {
                    it.exc = repository.getExcercise(it.categoryId, it.excId)
                    it
                }
            }.awaitAll()
            _dayList.postValue(list)
        }
    }





}