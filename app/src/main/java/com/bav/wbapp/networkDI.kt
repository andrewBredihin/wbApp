package com.bav.wbapp

import com.bav.core.auth.AuthenticationApi
import com.bav.core.api.AppTokenManager
import com.bav.core.api.TokenInterceptor
import com.bav.core.api.TokenManager
import com.bav.core.auth.AuthorizationRepository
import com.bav.core.auth.AuthorizationRepositoryImpl
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

fun networkModule() = module {

    single<TokenManager> { AppTokenManager(get()) }

    single { GsonBuilder().create() }
    single {
        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        val tokenInterceptor = TokenInterceptor(get())

        OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .addInterceptor(tokenInterceptor)
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .build()
    }
    single {
        Retrofit.Builder()
            .baseUrl("https://mobile-study-java.simbirsoft.dev/")
            .addConverterFactory(GsonConverterFactory.create(get()))
            .client(get())
            .build()
    }

    single<AuthenticationApi> {
        val retrofit: Retrofit = get()
        retrofit.create(AuthenticationApi::class.java)
    }

    single<AuthorizationRepository> { AuthorizationRepositoryImpl(get(), get()) }
}