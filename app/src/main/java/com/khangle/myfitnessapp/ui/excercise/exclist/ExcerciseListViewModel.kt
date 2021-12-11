package com.khangle.myfitnessapp.ui.excercise.exclist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.khangle.myfitnessapp.data.MyFitnessAppRepository
import com.khangle.myfitnessapp.model.Excercise
import com.khangle.myfitnessapp.model.ExcerciseCategory
import com.khangle.myfitnessapp.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExcerciseListViewModel @Inject constructor(private val repository: MyFitnessAppRepository): BaseViewModel() {
    private val _excerciseList = MutableLiveData<List<Excercise>>()
    val excerciseList: LiveData<List<Excercise>> = _excerciseList

    fun invalidateExcercise(excerciseCategory: ExcerciseCategory) {
        viewModelScope.launch(Dispatchers.IO) {
         handleResponse {
             val list: List<Excercise> = repository.loadExcerciseList(excerciseCategory.id)

             val excercises = list.map { exc ->
                 val a = async {
                     val ensure =  repository.getStatEnsureList(exc.id,excerciseCategory.id)
                     exc.achieveEnsure = Gson().toJson(ensure)
                     exc
                 }
                 a
             }.awaitAll()
             _excerciseList.postValue(excercises)
             excercises.forEach {
                 val a = it.achieveEnsure
             }
         }
        }
    }
}