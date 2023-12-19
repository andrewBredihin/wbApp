package com.bav.core.profile

data class ProfileResponseDataModel(
    val name: String,
    val email: String,
    val phone: String,
    val password: String? = null,
    val birthday: String? = null,
    val imageUrl: String? = null
)

data class ResponseProfile(
    val code: Int,
    val body: ProfileResponseDataModel? = null
)

data class ResponseProfileAvatarDataModel(
    val code: Int,
    val image: String? = null
)