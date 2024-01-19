package com.bav.core.promotions

import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface PromotionsApi {

    @GET("/api/v1/promotions/allPromotions")
    suspend fun allPromotions(): Response<List<Promotion>>

    @Multipart
    @POST("/api/v1/promotions/savePromotion")
    suspend fun savePromotion(
        @Part("promotionDto") body: PromotionRequest,
        @Part image: MultipartBody.Part
    ) : Response<ResponseBody>
}