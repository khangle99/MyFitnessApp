package com.khangle.myfitnessapp.ui.statistic

import androidx.lifecycle.*
import com.google.firebase.auth.FirebaseAuth
import com.khangle.myfitnessapp.data.MyFitnessAppRepository
import com.khangle.myfitnessapp.model.user.UserStat
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StatisticViewModel @Inject constructor(private val repository: MyFitnessAppRepository): ViewModel() {

    val statHistoryList: LiveData<List<UserStat>> = repository.getStatistic().asLiveData()
    private val uid = FirebaseAuth.getInstance().uid!!

    fun getStatHistory() {
        viewModelScope.launch(Dispatchers.IO) {

            repository.invalidateStatisticList(uid)
        }
    }

    fun addStat(stat: UserStat) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertStat(uid, stat)
        }
    }

    fun removeStat(stat: UserStat) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteStat(uid, stat.id)
        }
    }

}