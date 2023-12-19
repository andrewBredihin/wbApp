package com.bav.core.profile

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET

interface ProfileApi {

    @GET("/api/v1/profile/details")
    suspend fun loadProfile(): Response<ProfileResponseDataModel>

    // FIXME() почему то не видит этот запрос
    @GET("/api/v1/profile/downloadAvatar")
    suspend fun loadAvatar(): Response<ResponseBody>
}