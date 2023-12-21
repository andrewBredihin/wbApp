package com.bav.core.menu

interface MenuRepository {
    suspend fun loadAllProducts(): MenuResponseModel
}

class MenuRepositoryImpl(
    private val api: MenuApi
) : MenuRepository {

    override suspend fun loadAllProducts(): MenuResponseModel {
        val response = api.allProducts()
        return MenuResponseModel(
            code = response.code(),
            message = response.message(),
            body = response.body()
        )
    }
}