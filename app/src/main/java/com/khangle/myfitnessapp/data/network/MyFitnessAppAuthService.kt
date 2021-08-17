package com.khangle.myfitnessapp.data.network

import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface MyFitnessAppAuthService {
    @FormUrlEncoded
    @POST("registerUser")
    suspend fun registerUser(
        @Field("uid") uid: String,
        @Field("email") email: String
    ):
            ResponseMessage
}