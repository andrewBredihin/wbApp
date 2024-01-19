package com.bav.core.promotions

import com.bav.core.api.ResponseCode
import okhttp3.MultipartBody

interface PromotionRepository {
    suspend fun getAll(): List<Promotion>
    suspend fun save(
        data: PromotionRequest,
        image: MultipartBody.Part
    ): Boolean
}

class PromotionRepositoryImpl(
    private val api: PromotionsApi
) : PromotionRepository {

    override suspend fun getAll(): List<Promotion> {
        val result = api.allPromotions()
        result.body()?.let { return it } ?: return emptyList()
    }

    override suspend fun save(
        data: PromotionRequest,
        image: MultipartBody.Part
    ): Boolean {
        val result = api.savePromotion(data, image)
        return result.code() == ResponseCode.RESPONSE_SUCCESSFUL
    }
}