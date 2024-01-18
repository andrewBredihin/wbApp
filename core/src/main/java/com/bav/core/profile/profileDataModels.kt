package com.bav.core.profile

import android.graphics.Bitmap
import okhttp3.ResponseBody

data class ProfileResponseDataModel(
    val name: String,
    val email: String,
    val phone: String,
    val password: String? = null,
    val birthday: String? = null,
    val imageUrl: String? = null,
    val image: Bitmap? = null
)

data class ResponseProfile(
    val code: Int,
    val body: ProfileResponseDataModel? = null
)

data class ResponseProfileAvatarDataModel(
    val code: Int,
    val image: ResponseBody? = null
)

data class ProfileRequestBody(
    val name: String?,
    val phone: String?,
    val email: String,
    val password: String?,
    val birthday: String?
)