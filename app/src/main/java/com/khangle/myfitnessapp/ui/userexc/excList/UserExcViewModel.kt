package com.khangle.myfitnessapp.ui.userexc.excList

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.Gson
import com.khangle.myfitnessapp.data.MyFitnessAppRepository
import com.khangle.myfitnessapp.data.network.ResponseMessage
import com.khangle.myfitnessapp.model.user.PlanDay
import com.khangle.myfitnessapp.model.user.Session
import com.khangle.myfitnessapp.model.user.UserExcTuple
import com.khangle.myfitnessapp.model.user.UserExcercise
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import javax.inject.Inject


@HiltViewModel
class UserExcViewModel @Inject constructor(private val repository: MyFitnessAppRepository): ViewModel() {

    private val uid = FirebaseAuth.getInstance().uid!!

    private var _dayList = MutableLiveData<List<PlanDay>>()
    val dayList: LiveData<List<PlanDay>> = _dayList
    fun loadList() {
        viewModelScope.launch(Dispatchers.IO) {
            val list = repository.loadDayList(uid).map {
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