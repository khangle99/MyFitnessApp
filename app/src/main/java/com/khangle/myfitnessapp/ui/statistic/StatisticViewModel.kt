package com.khangle.myfitnessapp.ui.statistic

import androidx.lifecycle.*
import com.google.firebase.auth.FirebaseAuth
import com.khangle.myfitnessapp.common.toFormatDate
import com.khangle.myfitnessapp.data.MyFitnessAppRepository
import com.khangle.myfitnessapp.data.network.ResponseMessage
import com.khangle.myfitnessapp.model.user.UserStat
import com.khangle.myfitnessapp.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class StatisticViewModel @Inject constructor(private val repository: MyFitnessAppRepository): BaseViewModel() {

    val statHistoryList: LiveData<List<UserStat>> = repository.getStatistic().asLiveData().map {
        val sortedByDate = it.sortedWith(Comparator { o1, o2 ->
            val d1 = o1.dateString.toFormatDate()
            val d2 = o2.dateString.toFormatDate()
            if (d1!!.before(d2)) {
                return@Comparator -1
            } else if (d1.after(d2)) {
                return@Comparator 1
            } else {
                return@Comparator 0
            }
        })
        sortedByDate
    }

    private val uid = FirebaseAuth.getInstance().uid!!

    fun getStatHistory() {
        viewModelScope.launch(Dispatchers.IO) {
            handleResponse { repository.invalidateStatisticList(uid) }
        }
    }

    fun addStat(stat: UserStat, handle: (ResponseMessage) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            handleResponse(handle) {
                val res = repository.insertStat(uid, stat)
                withContext(Dispatchers.Main) {
                    handle(res)
                }
            }
        }
    }

    fun removeStat(stat: UserStat, handle: (ResponseMessage) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            handleResponse(handle) {
                repository.deleteStat(uid, stat.id)
            }
        }
    }

}