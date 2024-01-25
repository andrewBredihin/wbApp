package com.bav.wbapp.order.apply

import com.bav.core.basket.ProductEntity

sealed class ApplyOrderAction {
    data class SetChangeRequired(val value: Boolean) : ApplyOrderAction()
    data class SetMoneyAvailable(val value: Int) : ApplyOrderAction()
    data class SetDeliveryTime(val value: String) : ApplyOrderAction()
    data object StartLoading : ApplyOrderAction()
    data object CreateOrder : ApplyOrderAction()
}

data class ApplyOrderState(
    val menu: List<ProductEntity> = emptyList(),
    val changeRequired: Boolean = false,
    val moneyAvailable: Int = 0,
    val deliveryTime: String? = null,
    val isLoading: Boolean = true,
    val isCreatingOrder: Boolean = false,
    val isLoaded: Boolean = false
)
