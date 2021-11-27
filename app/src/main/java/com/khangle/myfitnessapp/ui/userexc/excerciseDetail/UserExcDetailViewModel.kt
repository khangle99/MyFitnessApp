package com.khangle.myfitnessapp.ui.userexc.excerciseDetail

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

//    private val _excerciseSession = repository.getUserSession().asLiveData(Dispatchers.IO)
//    val excerciseSession: LiveData<List<Session>> = _excerciseSession
//
//
//    fun addUserExcercise(userExcercise: UserExcercise, sessionId: String, handle: (ResponseMessage) -> Unit) {
//        viewModelScope.launch(Dispatchers.IO) {
//            val res = repository.insertUserExc(uid, userExcercise)
//            withContext(Dispatchers.Main) {
//                handle(res)
//            }
//
//        }
//    }
//
//    fun updateUserExcercise(userExcercise: UserExcercise, handle: (ResponseMessage) -> Unit) {
//        viewModelScope.launch(Dispatchers.IO) {
//            val res = repository.updateUserExc(uid, userExcercise)
//            withContext(Dispatchers.Main) {
//                handle(res)
//            }
//        }
//    }
//
//    fun deleteUserExcercise(userExcercise: UserExcercise, handle: (ResponseMessage) -> Unit)  {
//        viewModelScope.launch(Dispatchers.IO) {
//            val res = repository.deleteUserExc(uid, userExcercise.sessionId, userExcercise.id)
//            withContext(Dispatchers.Main) {
//                handle(res)
//            }
//        }
//    }


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