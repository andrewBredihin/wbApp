package com.bav.wbapp.order.apply

import com.bav.core.basket.ProductEntity

sealed class ApplyOrderAction {
    data class SetOrderPaymentType(val value: OrderPaymentType) : ApplyOrderAction()
    data class SetChangeRequired(val value: Boolean) : ApplyOrderAction()
    data class SetMoneyAvailable(val value: Int) : ApplyOrderAction()
    data object StartLoading : ApplyOrderAction()
    data object CreateOrder : ApplyOrderAction()
}

enum class OrderPaymentType {
    CARD_TO_COURIER,
    CARD_IN_APP,
    CASH_TO_COURIER
}

data class ApplyOrderState(
    val menu: List<ProductEntity> = emptyList(),
    val type: OrderPaymentType = OrderPaymentType.CARD_TO_COURIER,
    val changeRequired: Boolean = false,
    val moneyAvailable: Int = 0,
    val isLoading: Boolean = true,
    val isLoaded: Boolean = false
)
