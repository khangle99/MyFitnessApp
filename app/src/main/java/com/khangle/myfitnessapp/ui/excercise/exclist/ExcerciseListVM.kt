package com.khangle.myfitnessadmin.excercise.exclist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.khangle.myfitnessadmin.data.MyFitnessRepository
import com.khangle.myfitnessadmin.model.Excercise
import com.khangle.myfitnessadmin.model.ExcerciseCategory
import com.khangle.myfitnessadmin.model.ResponseMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ExcerciseListVM @Inject constructor(private val myFitnessRepository: MyFitnessRepository) :
    ViewModel() {
    private var _excList = MutableLiveData<List<Excercise>>()
    val excerciseList: LiveData<List<Excercise>> = _excList
    fun loadList(categoryId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val list = myFitnessRepository.loadExcerciseList(categoryId)
            _excList.postValue(list)
        }
    }

    fun createExcerciseCategory(
        name: String,
        uriString: String,
        handle: (ResponseMessage) -> Unit
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val res = myFitnessRepository.postExcerciseCategory(name, uriString)
            withContext(Dispatchers.Main) {
                handle(res)
            }
        }
    }

    fun updateCategory(
        id: String,
        name: String,
        uriString: String?,
        handle: (ResponseMessage) -> Unit
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val res = myFitnessRepository.updateExcerciseCategory(id, name, uriString)
            withContext(Dispatchers.Main) {
                handle(res)
            }
        }
    }

    fun deleteCategory(id: String, handle: (ResponseMessage) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val res = myFitnessRepository.deleteExcerciseCategory(id)
            withContext(Dispatchers.Main) {
                handle(res)
            }
        }
    }
}