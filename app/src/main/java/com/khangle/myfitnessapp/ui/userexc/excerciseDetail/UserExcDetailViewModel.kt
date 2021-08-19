package com.khangle.myfitnessapp.ui.userexc.excerciseDetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.khangle.myfitnessapp.data.MyFitnessAppRepository
import com.khangle.myfitnessapp.data.network.ResponseMessage
import com.khangle.myfitnessapp.model.user.UserExcTuple
import com.khangle.myfitnessapp.model.user.UserExcercise
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserExcDetailViewModel @Inject constructor(private val repository: MyFitnessAppRepository): ViewModel() {

    private val uid = FirebaseAuth.getInstance().uid!!

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
            handle(res)
        }
    }

    fun updateUserExcercise(userExcercise: UserExcercise, handle: (ResponseMessage) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val res = repository.updateUserExc(uid, userExcercise)
            handle(res)
        }
    }

    fun deleteUserExcercise(userExcercise: UserExcercise, handle: (ResponseMessage) -> Unit)  {
        viewModelScope.launch(Dispatchers.IO) {
            val res = repository.deleteUserExc(uid, userExcercise.sessionId, userExcercise.id)
            handle(res)
        }
    }
}