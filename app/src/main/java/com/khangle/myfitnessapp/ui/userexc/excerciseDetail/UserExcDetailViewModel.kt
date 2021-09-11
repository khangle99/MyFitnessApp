package com.khangle.myfitnessapp.ui.userexc.excerciseDetail

import androidx.lifecycle.*
import com.google.firebase.auth.FirebaseAuth
import com.khangle.myfitnessapp.data.MyFitnessAppRepository
import com.khangle.myfitnessapp.data.network.ResponseMessage
import com.khangle.myfitnessapp.model.user.Session
import com.khangle.myfitnessapp.model.user.UserExcTuple
import com.khangle.myfitnessapp.model.user.UserExcercise
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

@HiltViewModel
class UserExcDetailViewModel @Inject constructor(private val repository: MyFitnessAppRepository): ViewModel() {

    private val uid = FirebaseAuth.getInstance().uid!!

    private val _excerciseSession = repository.getUserSession().asLiveData(Dispatchers.IO)
    val excerciseSession: LiveData<List<Session>> = _excerciseSession


    fun addUserExcercise(userExcercise: UserExcercise, sessionId: String, handle: (ResponseMessage) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val res = repository.insertUserExc(uid, userExcercise)
            withContext(Dispatchers.Main) {
                handle(res)
            }

        }
    }

    fun updateUserExcercise(userExcercise: UserExcercise, handle: (ResponseMessage) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val res = repository.updateUserExc(uid, userExcercise)
            withContext(Dispatchers.Main) {
                handle(res)
            }
        }
    }

    fun deleteUserExcercise(userExcercise: UserExcercise, handle: (ResponseMessage) -> Unit)  {
        viewModelScope.launch(Dispatchers.IO) {
            val res = repository.deleteUserExc(uid, userExcercise.sessionId, userExcercise.id)
            withContext(Dispatchers.Main) {
                handle(res)
            }
        }
    }
}