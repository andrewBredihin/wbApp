package com.bav.wbapp.basket

import com.bav.core.basket.ProductEntity

sealed class BasketAction {
    data object LoadingAction : BasketAction()
    data class ActivatePromoCode(val code: String) : BasketAction()
}

enum class PromoCodeStatus {
    ERROR,
    ENTER,
    ACTIVE
}

data class BasketState(
    val data: List<ProductEntity> = emptyList(),
    val promoCode: String? = "",
    val promoCodeMessage: String = "",
    val isLoading: Boolean = false,
    val promoCodeStatus: PromoCodeStatus = PromoCodeStatus.ENTER
)
