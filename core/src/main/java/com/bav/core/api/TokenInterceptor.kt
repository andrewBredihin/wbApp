package com.bav.core.api

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response

class TokenInterceptor(private val tokenManager: TokenManager) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = runBlocking { tokenManager.getToken.first() }
        val request = token?.let {
            chain.request().newBuilder()
                .addHeader("Authorization", it)
                .build()
        } ?: chain.request()
        return chain.proceed(request)
    }
}