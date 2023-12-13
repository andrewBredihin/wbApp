package com.bav.core.auth

data class RequestBody(
    val name: String? = null,
    val phone: String? = null,
    val email: String,
    val password: String
)

data class ResponseBody(
    val accessToken: String? = null
)

data class ResponseDataModel(
    val code: Int?,
    val message: String?
)