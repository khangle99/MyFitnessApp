package com.khangle.myfitnessapp.ui.nutrition

import androidx.lifecycle.*
import com.google.firebase.auth.FirebaseAuth
import com.khangle.myfitnessapp.data.MyFitnessAppRepository
import com.khangle.myfitnessapp.model.Excercise
import com.khangle.myfitnessapp.model.ExcerciseCategory
import com.khangle.myfitnessapp.model.Menu
import com.khangle.myfitnessapp.model.NutritionCategory
import com.khangle.myfitnessapp.model.user.ExcLog
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NutritionViewModel @Inject constructor(private val repository: MyFitnessAppRepository): ViewModel() {

    private val uid = FirebaseAuth.getInstance().uid!!

    private val _excLogOfMonth = MutableLiveData<List<ExcLog>>()
    val excLogOfMonth: LiveData<List<ExcLog>> = _excLogOfMonth

    fun loadExcLogOfMonth(mYStr: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val res = repository.loadLogOfMonth(uid, mYStr)
            _excLogOfMonth.postValue(res)
        }
    }


}