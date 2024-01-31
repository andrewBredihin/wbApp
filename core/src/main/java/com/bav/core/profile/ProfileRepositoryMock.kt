package com.bav.core.profile

import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Response

class ProfileRepositoryMock : ProfileRepository {
    override suspend fun loadProfile(): ResponseProfile {
        return ResponseProfile(
            200,
            ProfileResponseDataModel(
                name = "Mock Mock",
                email = "mock@mail.ru",
                phone = "8 927 100-10-10",
                password = "123456",
                birthday = null,
                imageUrl = null,
                image = null
            )
        )
    }

    override suspend fun loadAvatar(): ResponseProfileAvatarDataModel {
        return ResponseProfileAvatarDataModel(200, null)
    }

    override suspend fun updateProfile(body: ProfileRequestBody): Response<ResponseBody> {
        return Response.success("Mock".toResponseBody("Text".toMediaType()))
    }

    override suspend fun uploadAvatar(image: MultipartBody.Part): Response<ResponseBody> {
        return Response.success("Mock".toResponseBody("Text".toMediaType()))
    }
}