package com.khangle.myfitnessapp.ui.userexc

import androidx.lifecycle.*
import com.google.firebase.auth.FirebaseAuth
import com.khangle.myfitnessapp.data.MyFitnessAppRepository
import com.khangle.myfitnessapp.data.network.ResponseMessage
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

    // session
    private val _excerciseSession = repository.getUserSession().asLiveData(Dispatchers.IO)
    val excerciseSession: LiveData<List<Session>> = _excerciseSession

    private val uid = FirebaseAuth.getInstance().uid!!

    fun invalidateExcSession() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.invalidateSessionList(uid)
        }
    }

    fun addExcSession(session: Session, handle: (ResponseMessage) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
           val res =  repository.insertSession(uid, session)
            withContext(Dispatchers.Main) {
                handle(res)
            }
        }
    }

    fun removeExcSession(sessionId: String, handle: (ResponseMessage) -> Unit) {
        viewModelScope.launch((Dispatchers.IO)) {
           handleResponse(handle) {
               val res = repository.deleteSession(uid, sessionId)
               withContext(Dispatchers.Main) {
                   handle(res)
               }
           }

        }
    }

}