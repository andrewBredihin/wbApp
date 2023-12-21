package com.bav.core.menu

import retrofit2.Response
import retrofit2.http.GET

interface MenuApi {

    @GET("/api/v1/products/allProducts")
    suspend fun allProducts(): Response<List<MenuDto>>
}