package com.bav.core.basket

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ProductEntity(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "product_id") val productId: Int,
    @ColumnInfo(name = "amount") val amount: Int,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "type") val type: String,
    @ColumnInfo(name = "price") val price: Float,
    @ColumnInfo(name = "amount_in_basket") val amountInBasket: Int,
    @ColumnInfo(name = "available") val available: Boolean
)