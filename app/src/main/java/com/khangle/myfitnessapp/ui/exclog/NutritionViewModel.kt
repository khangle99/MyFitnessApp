package com.khangle.myfitnessapp.ui.exclog

import androidx.lifecycle.*
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.Gson
import com.khangle.myfitnessapp.data.MyFitnessAppRepository
import com.khangle.myfitnessapp.model.Excercise
import com.khangle.myfitnessapp.model.user.ExcLog
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NutritionViewModel @Inject constructor(private val repository: MyFitnessAppRepository): ViewModel() {

    private val uid = FirebaseAuth.getInstance().uid!!

    private val _excLogOfMonth = MutableLiveData<List<ExcLog>>()
    val excLogOfMonth: LiveData<List<ExcLog>> = _excLogOfMonth

    private val _excercises = MutableLiveData<List<Excercise>>()
    val excercises: LiveData<List<Excercise>> = _excercises

    fun loadExcLogOfMonth(mYStr: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val res = repository.loadLogOfMonth(uid, mYStr)

            val excercise = res.map { excLog ->
                val a = async {
                    val excercise =  repository.getExcercise(excLog.categoryId,excLog.excId)
                    excercise
                }
                a
            }.awaitAll()
            _excercises.postValue(excercise)
            _excLogOfMonth.postValue(res)
        }
    }




}