package com.khangle.myfitnessapp.ui.nutrition.menulist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.khangle.myfitnessapp.data.MyFitnessAppRepository
import com.khangle.myfitnessapp.model.Menu
import com.khangle.myfitnessapp.model.NutritionCategory
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MenuListViewModel @Inject constructor(private val repository: MyFitnessAppRepository): ViewModel() {
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