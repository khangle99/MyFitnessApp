package com.khangle.myfitnessapp.ui.excercise.exclist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.khangle.myfitnessapp.data.MyFitnessAppRepository
import com.khangle.myfitnessapp.model.Excercise
import com.khangle.myfitnessapp.model.ExcerciseCategory
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExcerciseListViewModel @Inject constructor(private val repository: MyFitnessAppRepository): ViewModel() {
    private val _excerciseList = MutableLiveData<List<Excercise>>()
    val excerciseList: LiveData<List<Excercise>> = _excerciseList

    fun invalidateExcercise(excerciseCategory: ExcerciseCategory) {
        viewModelScope.launch {
            repository.invalidateExcerciseList(excerciseCategory.id)
            repository.getExcerciseList(excerciseCategory.id).collect {
                _excerciseList.postValue(it)
            }
        }
    }
}