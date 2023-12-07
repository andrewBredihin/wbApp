package com.bav.wbapp.main.model

sealed class MainCategoryModel {
    abstract val id: String

    data class MainCategoryBig(val title: String, val imageUrl: String, override val id: String) : MainCategoryModel()

    data class MainCategorySmall(val title: String, val imageUrl: String, override val id: String) : MainCategoryModel()
}