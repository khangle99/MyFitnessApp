package com.khangle.myfitnessapp.ui.excercise

import androidx.lifecycle.*
import com.khangle.myfitnessapp.data.MyFitnessAppRepository
import com.khangle.myfitnessapp.model.Excercise
import com.khangle.myfitnessapp.model.ExcerciseCategory
import com.khangle.myfitnessapp.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExcerciseViewModel @Inject constructor(private val repository: MyFitnessAppRepository): BaseViewModel() {

    private val _excerciseCategory = repository.getExcerciseCategory().asLiveData(Dispatchers.IO)
    val excerciseCategory: LiveData<List<ExcerciseCategory>> = _excerciseCategory

    fun invalidateExcCategory() {
       viewModelScope.launch(Dispatchers.IO) {
          handleResponse {
              repository.invalidateExcCategory()
          }
       }
    }

}