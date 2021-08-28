package com.khangle.myfitnessapp.data.db

import androidx.room.*
import com.khangle.myfitnessapp.model.Excercise
import com.khangle.myfitnessapp.model.ExcerciseCategory
import com.khangle.myfitnessapp.model.Menu
import com.khangle.myfitnessapp.model.NutritionCategory
import com.khangle.myfitnessapp.model.user.Session
import com.khangle.myfitnessapp.model.user.UserExcercise
import com.khangle.myfitnessapp.model.user.UserStat
import com.khangle.myfitnessapp.model.user.UserStep
import kotlinx.coroutines.flow.Flow

@Dao
interface MyFitnessDao {
    // category
    @Query("SELECT * FROM ExcerciseCategory")
    fun getExcCategory(): Flow<List<ExcerciseCategory>>

    @Insert
    suspend fun insertExcCategory(vararg excCategory: ExcerciseCategory)

    @Query("DELETE FROM ExcerciseCategory")
    suspend fun deleteAllExcCategory()

    @Transaction
    suspend fun invalidateExcCategory(vararg excCategory: ExcerciseCategory) {
        deleteAllExcCategory()
        insertExcCategory(*excCategory)
    }

    @Query("SELECT * FROM Excercise WHERE catId = :catId")
    fun getExcerciseByCatId(catId: String): Flow<List<Excercise>>

    @Insert
    suspend fun insertExcercise(vararg excercise: Excercise)

    @Query("DELETE FROM Excercise WHERE catId = :catId")
    suspend fun deleteAllExcerciseByCatId(catId: String)

    @Transaction
    suspend fun invalidateExcercise(catId: String,vararg excercise: Excercise) {
        deleteAllExcerciseByCatId(catId)
        val list = excercise.asList()
        list.forEach {
            it.catId = catId
        }
        insertExcercise(*list.toTypedArray())
    }

    // nutrition
    @Query("SELECT * FROM NutritionCategory")
    fun getNutCategory(): Flow<List<NutritionCategory>>

    @Insert
    suspend fun insertNutCategory(vararg nutritionCategory: NutritionCategory)

    @Query("DELETE FROM NutritionCategory")
    suspend fun deleteAllNutritionCategory()

    @Transaction
    suspend fun invalidateNutCategory(vararg nutritionCategory: NutritionCategory) {
        deleteAllNutritionCategory()
        insertNutCategory(*nutritionCategory)
    }

    @Query("SELECT * FROM Menu WHERE catId = :catId")
    fun getMenuByCatId(catId: String): Flow<List<Menu>>

    @Insert
    suspend fun insertMenu(vararg menu: Menu)

    @Query("DELETE FROM Menu WHERE catId = :catId")
    suspend fun deleteAllMenuByCatId(catId: String)

    @Transaction
    suspend fun invalidateMenu(catId: String, vararg menu: Menu) {
        deleteAllMenuByCatId(catId)
        val list = menu.asList()
        list.forEach {
            it.catId = catId
        }
        insertMenu(*list.toTypedArray())
    }

    // session
    @Query("SELECT * FROM Session")
    fun getSession(): Flow<List<Session>>

    @Insert
    suspend fun insertSession(vararg session: Session)

    @Update
    suspend fun updateSession(vararg session: Session)

    @Query("DELETE FROM Session")
    suspend fun deleteAllSession()

    @Query("DELETE FROM Session WHERE id = :sessionId")
    suspend fun deleteSession(sessionId: String)

    @Transaction
    suspend fun invalidateSession(vararg session: Session) {
        deleteAllSession()
        insertSession(*session)
    }

    // userexc
    @Query("SELECT * FROM UserExcercise WHERE sessionId = :sessionId")
    fun getUserExcerciseBySessionId(sessionId: String): Flow<List<UserExcercise>>

    @Insert
    suspend fun insertUserExcercise(vararg userExcercise: UserExcercise)

    @Update
    suspend fun updateUserExcercise(vararg userExcercise: UserExcercise)

    @Query("DELETE FROM UserExcercise WHERE sessionId = :sessionId")
    suspend fun deleteAllUserExcerciseBySessionId(sessionId: String)

    @Query("DELETE FROM UserExcercise WHERE sessionId = :sessionId AND id = :excId")
    suspend fun deleteUserExcerciseBySessionId(sessionId: String, excId: String)

    @Transaction
    suspend fun invalidateUserExcercise(sessionId: String, vararg userExcercise: UserExcercise) {
        deleteAllUserExcerciseBySessionId(sessionId)
        userExcercise.forEach {
            it.sessionId = sessionId
        }
        insertUserExcercise(*userExcercise)
    }
    // stat
    @Query("SELECT * FROM UserStat")
    fun getUserStat(): Flow<List<UserStat>>

    @Insert
    suspend fun insertUserStat(vararg userStat: UserStat)

    @Query("DELETE FROM UserStat")
    suspend fun deleteAllUserStat()

    @Transaction
    suspend fun invalidateStat(vararg userStat: UserStat) {
        deleteAllUserStat()
        insertUserStat(*userStat)
    }

    @Query("DELETE FROM UserStat WHERE id = :statId")
    suspend fun deleteUserStat(statId: String)


    //step
    @Query("SELECT * FROM UserStep")
    fun getUserStep(): Flow<List<UserStep>>

    @Insert
    suspend fun insertUserStep(vararg userStep: UserStep)

    @Query("DELETE FROM UserStep")
    suspend fun deleteAllUserStep()

    @Transaction
    suspend fun invalidateStep(vararg userStep: UserStep) {
        deleteAllUserStep()
        insertUserStep(*userStep)
    }
}