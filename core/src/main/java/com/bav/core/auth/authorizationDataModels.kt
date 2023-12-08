package com.bav.core.auth

data class RequestBody(
    val name: String?,
    val email: String,
    val password: String
)

data class ResponseBody(
    val accessToken: String
)

data class ResponseDataModel(
    val code: Int,
    val message: String?
)