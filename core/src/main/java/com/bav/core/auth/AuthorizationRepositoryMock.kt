package com.bav.core.auth

class AuthorizationRepositoryMock : AuthorizationRepository {
    override suspend fun register(request: RequestBody): ResponseDataModel {
        return ResponseDataModel(200, "registration mock")
    }

    override suspend fun login(request: RequestBody): ResponseDataModel {
        return ResponseDataModel(200, "login mock")
    }

    override suspend fun logout(): ResponseDataModel {
        return ResponseDataModel(200, "logout mock")
    }
}