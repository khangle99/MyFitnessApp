package com.khangle.myfitnessapp.data.network

import androidx.room.Delete
import com.khangle.myfitnessapp.model.*
import com.khangle.myfitnessapp.model.user.*
import retrofit2.http.*

interface MyFitnessAppService {
    /////////feed

    @GET("excercisesCategory")
    suspend fun fetchExcerciseCategory(): List<ExcerciseCategory>

    @GET("excercisesDetail")
    suspend fun fetchExcerciseDetail(
        @Query("categoryId") catId: String,
        @Query("id") id: String,
    ): Excercise


    @GET("excercises")
    suspend fun fetchExcerciseList(@Query("categoryId") catId: String): List<Excercise>

    @GET("excercisesDetail")
    suspend fun fetchExcercise(@Query("categoryId") catId: String, @Query("id") id: String): Excercise

    @GET("nutritionCategory")
    suspend fun fetchNutritionCategory(): List<NutritionCategory>

    @GET("menuList")
    suspend fun fetchMenuList(@Query("nutriId") nutriId: String): List<Menu>

    @GET("statEnsure")
    suspend fun getStatEnsureList(
        @Query("excId") id: String,
        @Query("catId") catId: String
    ): Map<String, Any> // moi string la json map chua cac statName va value

    //////////////stat

    @GET("userStatHistory")
    suspend fun fetchUserStat(@Query("uid") uid: String): List<UserStat>

    @FormUrlEncoded
    @POST("newUserStatHistory")
    suspend fun postStat(
        @Field("uid") uid: String,
        @Field("dateString") dateString: String,
        @Field("weight") weight: Int,
        @Field("height") height: Int,
    ):
            ResponseMessage

    @DELETE("deleteUserStatHistory")
    suspend fun deleteStat(
        @Query("uid") uid: String,
        @Query("statId") statId: String,
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
        @Field("steps") weight: Int,
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
        @Field("name") name: String,
    ):
            ResponseMessage

    @FormUrlEncoded
    @PUT("updateSession")
    suspend fun updateUserSession(
        @Field("uid") uid: String,
        @Field("name") name: String,
        @Field("sessionId") sessionId: String,
    ):
            ResponseMessage

    @DELETE("deleteSession")
    suspend fun deleteUserSession(
        @Query("uid") uid: String,
        @Query("sessionId") sessId: String,
    ):
            ResponseMessage
    /////////////userexcercise

    @GET("userExcercise")
    suspend fun fetchUserExcercise(
        @Query("uid") uid: String,
        @Query("sessionId") sessionId: String,
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
        @Field("excId") excId: String,
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
        @Field("noGap") noGap: String,
    ):
            ResponseMessage

    @DELETE("deleteUserExc")
    suspend fun deleteUserExcercise(
        @Query("uid") uid: String,
        @Query("sessionId") sessId: String,
        @Query("id") id: String,
    ):
            ResponseMessage

    @FormUrlEncoded
    @POST("increaseMenuView")
    suspend fun increaseView(
        @Field("nutriId") nutriId: String,
        @Field("id") id: String,
    ): ResponseMessage


    //////////// body stat
    @GET("bodyStats")
    suspend fun fetchBodyStat(): List<BodyStat>


    /////////// user plan
    @GET("userPlan")
    suspend fun fetchDayList(@Query("uid") uid: String): List<PlanDay>

    @FormUrlEncoded
    @PUT("updateExcPlanOnDay")
    suspend fun updatePlanDay(
        @Field("uid") uid: String,
        @Field("categoryId") categoryId: String,
        @Field("excId") excId: String,
        @Field("day") day: String,
        @Field("oldDay") oldDay: String
    ): ResponseMessage

    @DELETE("deleteExcPlanOnDay")
    suspend fun deletePlanDay(@Query("uid") uid: String,
                              @Query("day") day: String
    ): ResponseMessage

    ////////////// log excercise
    @GET("userExcLog")
    suspend fun fetchLogOfMonth(@Query("uid") uid: String, @Query("mYStr") mYStr: String): List<ExcLog>


    @FormUrlEncoded
    @POST("newHistoryMonth")
    suspend fun createMonth(  @Field("uid") uid: String,
                              @Field("mYStr") mYstr: String
    ): ResponseMessage

    @FormUrlEncoded
    @POST("newUserExcLog")
    suspend fun logDay(  @Field("uid") uid: String,
                         @Field("mYStr") mYstr: String,
                         @Field("excId") excId: String,
                         @Field("categoryId") categoryId: String,
                         @Field("dateInMonth") dateInMonth: String
    ): ResponseMessage

}

class ResponseMessage(val id: String?, val error: String?)