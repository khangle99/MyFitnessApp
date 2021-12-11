package com.khangle.myfitnessapp.ui.userexc.workout

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.khangle.myfitnessapp.data.MyFitnessAppRepository
import com.khangle.myfitnessapp.data.network.ResponseMessage
import com.khangle.myfitnessapp.model.user.PlanDay
import com.khangle.myfitnessapp.ui.base.BaseViewModel
import dagger.hilt.android.internal.Contexts.getApplication
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject

@HiltViewModel
class WorkoutViewModel @Inject constructor(private val repository: MyFitnessAppRepository): BaseViewModel(){

    private val uid = FirebaseAuth.getInstance().uid!!
    var currentWeight = MutableLiveData<Int>()

    fun logDay(planDay: PlanDay) {
        viewModelScope.launch(Dispatchers.IO) {
           handleResponse {
               val calendar = Calendar.getInstance(TimeZone.getDefault())
               val year = calendar.get(Calendar.YEAR) % 100
               val month = calendar.get(Calendar.MONTH) + 1
               val myStr = month.toString() + year.toString()
               val dateInMonth = calendar.get(Calendar.DAY_OF_MONTH).toString()
               repository.createMonth(uid, myStr) // override month neu da co
               val res = repository.logDay(uid,myStr,planDay.excId,planDay.categoryId,dateInMonth)
               withContext(Dispatchers.Main) {
                   if (res.id != null) {
                       print("")
                   } else {
                       print("")
                   }
               }
           }
        }
    }


    fun fetchCurrentWeight() {
        viewModelScope.launch(Dispatchers.IO) {
          handleResponse {
              repository.fetchStatisticList(uid).firstOrNull()?.let {
                  currentWeight.postValue(it.weight)
              }
          }

        }
    }
}