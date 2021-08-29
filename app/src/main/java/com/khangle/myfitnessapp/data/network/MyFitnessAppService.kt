package com.khangle.myfitnessapp.data.network

import androidx.room.Delete
import com.khangle.myfitnessapp.model.Excercise
import com.khangle.myfitnessapp.model.ExcerciseCategory
import com.khangle.myfitnessapp.model.Menu
import com.khangle.myfitnessapp.model.NutritionCategory
import com.khangle.myfitnessapp.model.user.Session
import com.khangle.myfitnessapp.model.user.UserExcercise
import com.khangle.myfitnessapp.model.user.UserStat
import com.khangle.myfitnessapp.model.user.UserStep
import retrofit2.http.*

interface MyFitnessAppService {
    /////////feed

    @GET("excercisesCategory")
    suspend fun fetchExcerciseCategory(): List<ExcerciseCategory>

    @GET("excercisesDetail")
    suspend fun fetchExcerciseDetail(@Query("categoryId") catId: String, @Query("id") id: String): Excercise


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
    suspend fun postStat(
        @Field("uid") uid: String,
        @Field("dateString") dateString: String,
        @Field("weight") weight: Int,
        @Field("height") height: Int
    ):
            ResponseMessage

    @DELETE("deleteUserStatHistory")
    suspend fun deleteStat(
        @Query("uid") uid: String,
        @Query("statId") statId: String
    ):
            ResponseMessage

    /////////////step
    @GET("userStep")
    suspend fun fetchUserStep(@Query("uid") uid: String): List<UserStep>

    @FormUrlEncoded
    @POST("newUserStep")
    suspend fun postStep(
        @Field("uid") uid: String,
        @Field("dateString") dateString: String,
        @Field("steps") weight: Int
    ):
            ResponseMessage

    @DELETE("deleteAllUserStep")
    suspend fun deleteAllStep(@Query("uid") uid: String):
            ResponseMessage


    /////////////session

    @GET("userSessions")
    suspend fun fetchUserSession(@Query("uid") uid: String): List<Session>

    @FormUrlEncoded
    @POST("newSession")
    suspend fun postUserSession(
        @Field("uid") uid: String,
        @Field("name") name: String
    ):
            ResponseMessage

    @FormUrlEncoded
    @PUT("updateSession")
    suspend fun updateUserSession(
        @Field("uid") uid: String,
        @Field("name") name: String,
        @Field("sessionId") sessionId: String
    ):
            ResponseMessage

    @DELETE("deleteSession")
    suspend fun deleteUserSession(
        @Query("uid") uid: String,
        @Query("sessionId") sessId: String
    ):
            ResponseMessage
    /////////////userexcercise

    @GET("userExcercise")
    suspend fun fetchUserExcercise(
        @Query("uid") uid: String,
        @Query("sessionId") sessionId: String
    ): List<UserExcercise>

    @FormUrlEncoded
    @POST("newUserExc")
    suspend fun postUserExcercise(
        @Field("uid") uid: String,
        @Field("sessionId") sessionID: String,
        @Field("noTurn") noTurn: String,
        @Field("noSec") noSec: String,
        @Field("noGap") noGap: String,
        @Field("categoryId") categoryId: String,
        @Field("excId") excId: String
    ):
            ResponseMessage

    @FormUrlEncoded
    @PUT("updateUserExc")
    suspend fun updateUserExcercise(
        @Field("uid") uid: String,
        @Field("sessionId") sessionID: String,
        @Field("id") id: String,
        @Field("noTurn") noTurn: String,
        @Field("noSec") noSec: String,
        @Field("noGap") noGap: String
    ):
            ResponseMessage

    @DELETE("deleteUserExc")
    suspend fun deleteUserExcercise(
        @Query("uid") uid: String,
        @Query("sessionId") sessId: String,
        @Query("id") id: String,
    ):
            ResponseMessage

}

class ResponseMessage(val id: String?, val error: String?)