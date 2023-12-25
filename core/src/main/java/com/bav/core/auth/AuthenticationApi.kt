package com.bav.core.auth

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthenticationApi {

    @POST("/api/v1/auth/register")
    suspend fun registration(@Body requestBody: RequestBody): Response<okhttp3.ResponseBody>

    @POST("/api/v1/auth/login")
    suspend fun login(@Body requestBody: RequestBody): Response<ResponseBody>

    /**
     * Если возникнут проблемы - заменить ResponseBody на okhttp3.ResponseBody
     */
    @POST("/api/v1/auth/logout")
    suspend fun logout(): Response<ResponseBody>
}