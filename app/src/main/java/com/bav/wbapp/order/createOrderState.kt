package com.bav.wbapp.order

import java.time.LocalDateTime

sealed class CreateOrderAction {
    data object LoadingAction : CreateOrderAction()
    data object Loaded : CreateOrderAction()
    data class SetOrderType(val type: OrderType) : CreateOrderAction()
    data class SetName(val name: String) : CreateOrderAction()
    data class SetLastName(val lastName: String) : CreateOrderAction()
    data class SetPhone(val phone: String) : CreateOrderAction()
    data class SetEmail(val email: String) : CreateOrderAction()
    data class SetAddress(val address: String) : CreateOrderAction()
    data class SetEntrance(val entrance: String) : CreateOrderAction()
    data class SetCode(val code: String) : CreateOrderAction()
    data class SetFloor(val floor: String) : CreateOrderAction()
    data class SetApartment(val apartment: String) : CreateOrderAction()
    data class SetDate(val date: LocalDateTime) : CreateOrderAction()
    data class SetCommentary(val commentary: String) : CreateOrderAction()
}

enum class OrderType {
    DELIVERY,
    TIME_IN,
    PICKUP
}

data class CreateOrderState(
    val name: String = "",
    val lastName: String? = "",
    val phone: String = "",
    val email: String = "",
    val date: LocalDateTime? = null,
    val address: String = "",
    val entrance: String? = "",
    val code: String? = "",
    val floor: String? = "",
    val apartment: String? = "",
    val commentary: String = "",
    val type: OrderType = OrderType.DELIVERY,
    val isLoading: Boolean = false,
    val continueEnabled: Boolean = false,
    val isLoaded: Boolean = false
)
