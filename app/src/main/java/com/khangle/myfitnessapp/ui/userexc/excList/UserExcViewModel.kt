package com.khangle.myfitnessapp.ui.userexc.excList

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
class UserExcViewModel @Inject constructor(private val repository: MyFitnessAppRepository): ViewModel() {

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

    fun updateSession(session: Session, handle: (ResponseMessage) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val res =  repository.updateSession(uid, session)
            withContext(Dispatchers.Main) {
                handle(res)
            }
        }
    }


}