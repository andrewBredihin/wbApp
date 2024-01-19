package com.bav.core.promotions

data class Promotion(
    val id: Int,
    val title: String,
    val description: String,
    val date: String
)

data class PromotionRequest(
    val title: String,
    val description: String,
    val date: String
)