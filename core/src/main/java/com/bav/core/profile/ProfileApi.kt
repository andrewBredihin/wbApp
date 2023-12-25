package com.bav.core.profile

import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ProfileApi {

    @GET("/api/v1/profile/details")
    suspend fun loadProfile(): Response<ProfileResponseDataModel>

    @POST("/api/v1/profile/updateUserInfo")
    suspend fun updateProfile(@Body requestBody: ProfileRequestBody): Response<ResponseBody>

    @GET("/api/v1/profile/downloadAvatar")
    suspend fun loadAvatar(): Response<ResponseBody>

    @Multipart
    @POST("/api/v1/profile/uploadAvatar")
    suspend fun uploadAvatar(@Part image: MultipartBody.Part): Response<ResponseBody>
}