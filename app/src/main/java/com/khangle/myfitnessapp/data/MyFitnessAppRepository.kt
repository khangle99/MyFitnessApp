package com.khangle.myfitnessapp.data

import com.google.firebase.auth.FirebaseAuth
import com.khangle.myfitnessapp.data.db.MyFitnessDao
import com.khangle.myfitnessapp.data.network.MyFitnessAppService
import com.khangle.myfitnessapp.model.Excercise
import com.khangle.myfitnessapp.model.ExcerciseCategory
import com.khangle.myfitnessapp.model.Menu
import com.khangle.myfitnessapp.model.NutritionCategory
import com.khangle.myfitnessapp.model.user.UserStat
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class  MyFitnessAppRepository @Inject constructor(private val service: MyFitnessAppService, private val dao: MyFitnessDao){
     fun getExcerciseCategory(): Flow<List<ExcerciseCategory>> {
        return dao.getExcCategory()
    }

    suspend fun invalidateExcCategory() {
        val excerciseCategory = service.fetchExcerciseCategory()
        dao.invalidateExcCategory(*excerciseCategory.toTypedArray())
    }

    fun getExcerciseList(catId: String): Flow<List<Excercise>> {
        return dao.getExcerciseByCatId(catId)
    }
    suspend fun invalidateExcerciseList(catId: String) {
        val excercises = service.fetchExcerciseList(catId)
        dao.invalidateExcercise(catId,*excercises.toTypedArray())
    }

    fun getNutritionCategory(): Flow<List<NutritionCategory>> {
        return dao.getNutCategory()
    }
    suspend fun invalidateNutCategory() {
        val nutritionCategory = service.fetchNutritionCategory()
        dao.invalidateNutCategory(*nutritionCategory.toTypedArray())
    }

    fun getMenuList(catId: String): Flow<List<Menu>> {
        return dao.getMenuByCatId(catId)
    }
    suspend fun invalidateMenuList(nutriId: String) {
        val menuList = service.fetchMenuList(nutriId)
        dao.invalidateMenu(nutriId,*menuList.toTypedArray())
    }

    //======================== statistic
    fun getStatistic(): Flow<List<UserStat>> {
        return dao.getUserStat()
    }

    suspend fun invalidateStatisticList(uid: String) {
        val statList = service.fetchUserStat(uid)
        dao.invalidateStat(*statList.toTypedArray())
    }

    suspend fun deleteStat(uid: String, statId: String) {
        service.deleteStat(uid, statId)
        dao.deleteUserStat(statId)
    }

    suspend fun insertStat(uid: String, stat: UserStat) {
        service.postStat(uid, stat.dateString, stat.weight, stat.height)
        dao.insertUserStat(stat)
    }

}