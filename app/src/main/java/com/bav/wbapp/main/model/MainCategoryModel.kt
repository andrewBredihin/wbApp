package com.bav.wbapp.main.model

sealed class MainCategoryModel {
    abstract val id: String

    companion object {
        const val STOCK = "1"
        const val DELIVERY = "2"
        const val INFO = "3"
        const val MENU = "4"
        const val PROFILE = "5"
        const val RESTAURANTS = "6"
        const val REVIEW = "7"
        const val HISTORY = "8"
    }

    data class MainCategoryBig(
        val title: String,
        val imageUrl: String,
        override val id: String,
    ) : MainCategoryModel()

    data class MainCategorySmall(
        val title: String,
        val imageUrl: String,
        override val id: String
    ) : MainCategoryModel()
}