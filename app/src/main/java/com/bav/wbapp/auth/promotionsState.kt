package com.bav.wbapp.auth

import com.bav.core.promotions.Promotion

sealed class PromotionsAction {
    data object LoadingAction : PromotionsAction()
}

data class PromotionsState(
    val data: List<Promotion> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String = ""
)
