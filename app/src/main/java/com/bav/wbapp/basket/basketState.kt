package com.bav.wbapp.basket

import com.bav.core.basket.ProductEntity

sealed class BasketAction {
    data object LoadingAction : BasketAction()
}

data class BasketState(
    val data: List<ProductEntity> = emptyList(),
    val isLoading: Boolean = false
)
