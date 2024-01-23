package com.bav.wbapp.menu.model

data class MenuItemModel(
    val id: Int,
    val amount: Int,
    val amountInBasket: Int,
    val price: Float,
    val title: String,
    val description: String,
    val type: String
)