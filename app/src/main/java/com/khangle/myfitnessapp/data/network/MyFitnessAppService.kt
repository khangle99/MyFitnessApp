package com.khangle.myfitnessapp.data.network

import com.khangle.myfitnessapp.model.Excercise
import com.khangle.myfitnessapp.model.ExcerciseCategory
import com.khangle.myfitnessapp.model.Menu
import com.khangle.myfitnessapp.model.NutritionCategory
import retrofit2.http.GET
import retrofit2.http.Query

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




    /////////////session





    /////////////userexcercise





    /////////////step


}