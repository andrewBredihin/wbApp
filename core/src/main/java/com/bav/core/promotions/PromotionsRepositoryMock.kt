package com.bav.core.promotions

import okhttp3.MultipartBody

class PromotionsRepositoryMock : PromotionRepository {
    override suspend fun getAll(): List<Promotion> {
        return listOf(
            Promotion(
                id = 1,
                title = "Title 1",
                description = "Description 1",
                date = "2024-01-31T10:10:35.135064"
            ),
            Promotion(
                id = 2,
                title = "Title 2",
                description = "Description 2",
                date = "2024-01-31T10:10:35.135064"
            ),
            Promotion(
                id = 3,
                title = "Title 3",
                description = "Description 3",
                date = "2024-01-31T10:10:35.135064"
            ),
            Promotion(
                id = 4,
                title = "Title 4",
                description = "Description 4",
                date = "2024-01-31T10:10:35.135064"
            ),
            Promotion(
                id = 5,
                title = "Title 5",
                description = "Description 5",
                date = "2024-01-31T10:10:35.135064"
            )
        )
    }

    override suspend fun save(data: PromotionRequest, image: MultipartBody.Part): Boolean {
        return true
    }
}