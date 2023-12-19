package com.bav.core.profile

interface ProfileRepository {
    suspend fun loadProfile(): ResponseProfile
    suspend fun loadAvatar(): ResponseProfileAvatarDataModel
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
        return ResponseProfileAvatarDataModel(
            code = response.code(),
            image = ""
        )
    }
}