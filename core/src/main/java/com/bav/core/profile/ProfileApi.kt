package com.bav.core.profile

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ProfileApi {

    @GET("/api/v1/profile/details")
    suspend fun loadProfile(): Response<ProfileResponseDataModel>

    @POST("/api/v1/updateUserInfo")
    suspend fun updateProfile(@Body requestBody: ProfileRequestBody): Response<ResponseBody>

    @GET("/api/v1/downloadAvatar")
    suspend fun loadAvatar(): Response<ResponseBody>
}