package com.bav.core.auth

import com.bav.core.api.ResponseCode
import com.bav.core.api.TokenManager

interface AuthorizationRepository {
    suspend fun register(request: RequestBody): ResponseDataModel

    suspend fun login(request: RequestBody): ResponseDataModel

    suspend fun logout(): ResponseDataModel
}

class AuthorizationRepositoryImpl(
    private val api: AuthenticationApi,
    private val tokenManager: TokenManager
) : AuthorizationRepository {

    override suspend fun register(request: RequestBody): ResponseDataModel {
        val response = api.registration(request)
        return ResponseDataModel(
            response.code(),
            response.message()
        )
    }

    override suspend fun login(request: RequestBody): ResponseDataModel {
        val response = api.login(request)
        if (response.code() == ResponseCode.RESPONSE_SUCCESSFUL) {
            response.body()?.accessToken?.let {tokenManager.saveToken(it) }
        }
        return ResponseDataModel(
            response.code(),
            response.message()
        )
    }

    override suspend fun logout(): ResponseDataModel {
        val response = api.logout()
        tokenManager.removeToken()
        return ResponseDataModel(
            response.code(),
            response.message()
        )
    }

}