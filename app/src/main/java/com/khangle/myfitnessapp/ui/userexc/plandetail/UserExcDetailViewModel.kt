package com.khangle.myfitnessapp.ui.userexc.plandetail

import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.khangle.myfitnessapp.data.MyFitnessAppRepository
import com.khangle.myfitnessapp.data.network.ResponseMessage
import com.khangle.myfitnessapp.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class UserExcDetailViewModel @Inject constructor(private val repository: MyFitnessAppRepository): BaseViewModel() {

    private val uid = FirebaseAuth.getInstance().uid!!

    fun updatePlanDay(categoryId: String, excId: String, day: String, oldDay: String, handle: (ResponseMessage) -> Unit){
        viewModelScope.launch(Dispatchers.IO) {
            handleResponse(handle) {
                val res = repository.updatePlanDay(uid,categoryId,excId,day,oldDay)
                withContext(Dispatchers.Main) {
                    handle(res)
                }
            }
        }
    }

    fun deletePlanDay(day: String, handle: (ResponseMessage) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            handleResponse(handle) {
                val res = repository.deletePlanDay(uid, day)
                withContext(Dispatchers.Main) {
                    handle(res)
                }
            }
        }
    }
}