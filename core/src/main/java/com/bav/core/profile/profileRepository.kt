package com.bav.core.profile

import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Response

interface ProfileRepository {
    suspend fun loadProfile(): ResponseProfile
    suspend fun loadAvatar(): ResponseProfileAvatarDataModel
    suspend fun updateProfile(body: ProfileRequestBody): Response<ResponseBody>
    suspend fun uploadAvatar(image: MultipartBody.Part): Response<ResponseBody>
}

class ProfileRepositoryImpl(
    private val api: ProfileApi
) : ProfileRepository {

    override suspend fun loadProfile(): ResponseProfile {
        val response = api.loadProfile()
        return ResponseProfile(
            code = response.code(),
            body = response.body()
        )
    }

    override suspend fun loadAvatar(): ResponseProfileAvatarDataModel {
        val response = api.loadAvatar()
        // FIXME()
        return ResponseProfileAvatarDataModel(
            code = response.code(),
            image = "https://www.mtsolar.us/wp-content/uploads/2020/04/avatar-placeholder.png"
        )
    }

    override suspend fun updateProfile(body: ProfileRequestBody): Response<ResponseBody> {
        return api.updateProfile(body)
    }

    override suspend fun uploadAvatar(image: MultipartBody.Part): Response<ResponseBody> {
        return api.uploadAvatar(image)
    }
}