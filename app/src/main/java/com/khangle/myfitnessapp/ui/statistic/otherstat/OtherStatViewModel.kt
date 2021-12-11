package com.khangle.myfitnessapp.ui.statistic.otherstat

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.khangle.myfitnessapp.data.MyFitnessAppRepository
import com.khangle.myfitnessapp.data.network.ResponseMessage
import com.khangle.myfitnessapp.model.AppBodyStat
import com.khangle.myfitnessapp.model.BodyStat
import com.khangle.myfitnessapp.model.user.PlanDay
import com.khangle.myfitnessapp.model.user.UserStat
import com.khangle.myfitnessapp.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.http.Body
import javax.inject.Inject

@HiltViewModel
class OtherStatViewModel @Inject constructor(private val repository: MyFitnessAppRepository): BaseViewModel() {

    private val uid = FirebaseAuth.getInstance().uid!!

    private var _appBodyStatList =  MutableLiveData<List<AppBodyStat>>()
    val appBodyStatList: LiveData<List<AppBodyStat>> = _appBodyStatList
    fun getAppBodyStatList() {
        viewModelScope.launch(Dispatchers.IO) {
            handleResponse {
                _appBodyStatList.postValue(repository.getAppBodyStat())
            }
        }
    }

    private var _bodyStatList = MutableLiveData<List<BodyStat>>()
    val bodyStatList: LiveData<List<BodyStat>> = _bodyStatList

    fun getStatHistory() {
        viewModelScope.launch(Dispatchers.IO) {
           handleResponse {
               val res = repository.getBodyStat(uid)
               withContext(Dispatchers.Main) {
                   _bodyStatList.value = res
               }
           }

        }
    }

    fun addOtherStat(stat: BodyStat, handle: (ResponseMessage) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            handleResponse(handle) {
                val res = repository.inserOthertStat(uid, stat)
                withContext(Dispatchers.Main) {
                    handle(res)
                }
            }
        }
    }


    fun removeOtherStat(stat: BodyStat, handle: (ResponseMessage) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
           handleResponse(handle) {
               val res =  repository.deleteOtherStat(uid, stat.id)
               withContext(Dispatchers.Main) {
                   handle(res)
               }
           }

        }
    }
}