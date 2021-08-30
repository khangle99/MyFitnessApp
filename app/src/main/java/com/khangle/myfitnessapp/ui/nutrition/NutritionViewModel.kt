package com.khangle.myfitnessapp.ui.nutrition

import androidx.lifecycle.*
import com.khangle.myfitnessapp.data.MyFitnessAppRepository
import com.khangle.myfitnessapp.model.Excercise
import com.khangle.myfitnessapp.model.ExcerciseCategory
import com.khangle.myfitnessapp.model.Menu
import com.khangle.myfitnessapp.model.NutritionCategory
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NutritionViewModel @Inject constructor(private val repository: MyFitnessAppRepository): ViewModel() {
    private val _nutCategory = repository.getNutritionCategory().asLiveData(Dispatchers.IO)
    val nutCategory: LiveData<List<NutritionCategory>> = _nutCategory

    fun invalidateNutCategory() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.invalidateNutCategory()
        }
    }

    private val _menuList = MutableLiveData<List<Menu>>()
    val menuList: LiveData<List<Menu>> = _menuList

    fun invalidateMenu(nutCategory: NutritionCategory) {
        viewModelScope.launch {
            repository.invalidateMenuList(nutCategory.id)
            repository.getMenuList(nutCategory.id).collect {
                _menuList.postValue(it)
            }
        }
    }
    fun increaseView(menu: Menu) {
        viewModelScope.launch {
            repository.increaseView(menu.catId, menu.id)
        }
    }
}