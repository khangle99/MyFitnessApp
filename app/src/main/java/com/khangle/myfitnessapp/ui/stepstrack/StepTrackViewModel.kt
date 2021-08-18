package com.khangle.myfitnessapp.ui.stepstrack

import androidx.lifecycle.*
import com.google.firebase.auth.FirebaseAuth
import com.khangle.myfitnessapp.common.toFormatDate
import com.khangle.myfitnessapp.data.MyFitnessAppRepository
import com.khangle.myfitnessapp.data.network.ResponseMessage
import com.khangle.myfitnessapp.model.user.UserStep
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StepTrackViewModel @Inject constructor(private val repository: MyFitnessAppRepository): ViewModel() {

    val stepHistoryList: LiveData<List<UserStep>> = repository.getStepTrack().asLiveData().map {
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

    fun getStepHistory() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.invalidateStepList(uid)
        }
    }

    fun addStep(step: UserStep, handle: (ResponseMessage) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val res = repository.insertStep(uid, step)
            handle(res)
        }
    }

}