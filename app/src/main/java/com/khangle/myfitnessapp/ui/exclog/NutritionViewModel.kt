package com.khangle.myfitnessapp.ui.exclog

import androidx.lifecycle.*
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.Gson
import com.khangle.myfitnessapp.common.toFormatDate
import com.khangle.myfitnessapp.data.MyFitnessAppRepository
import com.khangle.myfitnessapp.model.AppBodyStat
import com.khangle.myfitnessapp.model.BodyStat
import com.khangle.myfitnessapp.model.Excercise
import com.khangle.myfitnessapp.model.user.ExcLog
import com.khangle.myfitnessapp.model.user.UserStat
import com.khangle.myfitnessapp.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import javax.inject.Inject

@HiltViewModel
class NutritionViewModel @Inject constructor(private val repository: MyFitnessAppRepository): BaseViewModel() {

    private val uid = FirebaseAuth.getInstance().uid!!

    private val _excLogOfMonth = MutableLiveData<List<ExcLog>>() // trong 1 thang
    val excLogOfMonth: LiveData<List<ExcLog>> = _excLogOfMonth

    private val _excercises = MutableLiveData<List<Excercise>>()
    val excercises: LiveData<List<Excercise>> = _excercises

    fun loadExcLogOfMonth(mYStr: String) {
        viewModelScope.launch(Dispatchers.IO) {
           handleResponse {
               val res = repository.loadLogOfMonth(uid, mYStr)

               val excercises = res.map { excLog ->
                   val a = async {
                       val excercise =  repository.getExcercise(excLog.categoryId,excLog.excId)
                       val ensure = repository.getStatEnsureList(excLog.excId,excLog.categoryId)
                       excercise.achieveEnsure = Gson().toJson(ensure)
                       excercise
                   }
                   a
               }.awaitAll()
               _excercises.postValue(excercises)
               _excLogOfMonth.postValue(res)
           }
        }
    }

    private var _appBodyStatList =  MutableLiveData<List<AppBodyStat>>()
    val appBodyStatList: LiveData<List<AppBodyStat>> = _appBodyStatList
    fun getAppBodyStatList() {
        viewModelScope.launch(Dispatchers.IO) {
            handleResponse {
                _appBodyStatList.postValue(repository.getAppBodyStat())
            }
        }
    }

    // toan bo tat ca life time nen can filter samemonth
    private var _bodyStatList = MutableLiveData<List<BodyStat>>() // dung de lay ra lastest record de rating, phai sorted truoc
    val bodyStatList: LiveData<List<BodyStat>> = _bodyStatList

    fun getStatHistory() {
        viewModelScope.launch(Dispatchers.IO) {
        handleResponse {
            val res = repository.getBodyStat(uid)
            withContext(Dispatchers.Main) {
                val filter = res.sortedWith(Comparator { o1, o2 ->
                    val d1 = o1.dateString.toFormatDate()
                    val d2 = o2.dateString.toFormatDate()
                    if (d1!!.before(d2)) {
                        return@Comparator 1
                    } else if (d1.after(d2)) {
                        return@Comparator -1
                    } else {
                        return@Comparator 0
                    }
                })
                _bodyStatList.value = filter
            }
        }

        }
    }
    private var _userStats = MutableLiveData<List<UserStat>>()
    val userStat: LiveData<List<UserStat>> = _userStats
    fun fetchWHStat() {
        viewModelScope.launch(Dispatchers.IO) {
            handleResponse {
                repository.fetchStatisticList(uid).sortedWith(Comparator { o1, o2 ->
                    val d1 = o1.dateString.toFormatDate()
                    val d2 = o2.dateString.toFormatDate()
                    if (d1!!.before(d2)) {
                        return@Comparator 1
                    } else if (d1.after(d2)) {
                        return@Comparator -1
                    } else {
                        return@Comparator 0
                    }
                })?.let {
                    _userStats.postValue(it)
                }
            }
        }
    }



}