package com.bav.wbapp.basket

import com.bav.core.basket.ProductEntity

sealed class BasketAction {
    data object LoadingAction : BasketAction()
    data class ActivatePromoCode(val code: String) : BasketAction()
    data class SetGuestsAmount(val amount: Int) : BasketAction()
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
    val discount: Int = 10,
    val price: Int = 0,
    val guestsAmount: Int = 0,
    val isLoading: Boolean = false,
    val promoCodeStatus: PromoCodeStatus = PromoCodeStatus.ENTER
)
