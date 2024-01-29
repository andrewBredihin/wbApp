package com.bav.wbapp.order.basket

import com.bav.core.basket.ProductEntity

data class ProductInBasket(
    val productId: Int,
    val amount: Int,
    val title: String,
    val type: String,
    val price: Float
) {
    fun toEntity() = ProductEntity(
        id = 0,
        productId = this.productId,
        amount = this.amount,
        title = this.title,
        type = this.type,
        price = this.price,
        amountInBasket = 1,
        available = true
    )
}