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
class UserExcViewModel @Inject constructor(private val repository: MyFitnessAppRepository): BaseViewModel() {

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


    fun updateSession(session: Session, handle: (ResponseMessage) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val res =  repository.updateSession(uid, session)
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

    // excercise

    private val _excerciseTuple = MutableLiveData<List<UserExcTuple>>()
    val excerciseTuple: LiveData<List<UserExcTuple>> = _excerciseTuple

    fun invalidateUserExcercise(sessionId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.invalidateUserExcList(uid, sessionId)
            repository.getUserExc(sessionId).collect {
                val list: List<UserExcTuple> = it.map {
                    async {
                        val excercise = repository.fetchExcercise(it.categoryId, it.excId)
                        UserExcTuple(it, excercise)
                    }
                }.awaitAll()
                _excerciseTuple.postValue(list)
            }
        }
    }

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