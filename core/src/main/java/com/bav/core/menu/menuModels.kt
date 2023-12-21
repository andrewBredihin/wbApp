package com.bav.core.menu

data class MenuResponseModel(
    val code: Int,
    val message: String?,
    val body: List<MenuDto>?
)

data class MenuDto(
    val id: Int,
    val amount: Int,
    val title: String,
    val description: String,
    val type: String
)