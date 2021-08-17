package com.khangle.myfitnessapp.data.network

import androidx.room.Delete
import com.khangle.myfitnessapp.model.Excercise
import com.khangle.myfitnessapp.model.ExcerciseCategory
import com.khangle.myfitnessapp.model.Menu
import com.khangle.myfitnessapp.model.NutritionCategory
import com.khangle.myfitnessapp.model.user.UserStat
import retrofit2.http.*

interface MyFitnessAppService {
    /////////feed

    @GET("excercisesCategory")
    suspend fun fetchExcerciseCategory(): List<ExcerciseCategory>

    @GET("excercises")
    suspend fun fetchExcerciseList(@Query("categoryId") catId: String): List<Excercise>

    @GET("nutritionCategory")
    suspend fun fetchNutritionCategory(): List<NutritionCategory>

    @GET("menuList")
    suspend fun fetchMenuList(@Query("nutriId") nutriId: String): List<Menu>

    //////////////stat

    @GET("userStatHistory")
    suspend fun fetchUserStat(@Query("uid") uid: String): List<UserStat>

    @FormUrlEncoded
    @POST("newUserStatHistory")
    suspend fun postStat(@Field("uid") uid: String,
                         @Field("dateString") dateString: String,
                         @Field("weight") weight: Int,
                         @Field("height") height: Int):
            ResponseMessage

    @DELETE("deleteUserStatHistory")
    suspend fun deleteStat(@Query("uid") uid: String,
                           @Query("statId") statId: String):
            ResponseMessage
    /////////////session





    /////////////userexcercise





    /////////////step


}

class ResponseMessage(val id: String?,val error: String?)