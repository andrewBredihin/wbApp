package com.bav.wbapp.order.apply

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bav.core.basket.ProductDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ApplyOrderViewModel(private val productDao: ProductDao) : ViewModel() {

    private val _applyOrderState: MutableStateFlow<ApplyOrderState> = MutableStateFlow(ApplyOrderState())
    val applyOrderState = _applyOrderState.asStateFlow()

    fun startAction(action: ApplyOrderAction) {
        when (action) {
            is ApplyOrderAction.SetChangeRequired   -> {
                _applyOrderState.value = _applyOrderState.value.copy(changeRequired = action.value)
            }

            is ApplyOrderAction.SetMoneyAvailable   -> {
                _applyOrderState.value = _applyOrderState.value.copy(moneyAvailable = action.value)
            }

            ApplyOrderAction.StartLoading           -> {
                loadingMenu()
            }

            ApplyOrderAction.CreateOrder             -> {
                _applyOrderState.value = _applyOrderState.value.copy(isCreatingOrder = true)
                createOrder()
            }

            is ApplyOrderAction.SetDeliveryTime -> {
                _applyOrderState.value = _applyOrderState.value.copy(deliveryTime = action.value)
            }
        }
    }

    private fun loadingMenu() {
        viewModelScope.launch(Dispatchers.IO) {
            val list = productDao.getAll()
            withContext(Dispatchers.Main) {
                _applyOrderState.value = _applyOrderState.value.copy(
                    isLoading = false,
                    menu = list
                )
            }
        }
    }

    /** Запрос в сеть - оформление заказа */
    private fun createOrder() {
        viewModelScope.launch(Dispatchers.Default) {
            delay(3000)
            productDao.deleteAll()
            withContext(Dispatchers.Main) {
                _applyOrderState.value = _applyOrderState.value.copy(isLoaded = true)
            }
        }
    }

    fun getMenuAmount(): Int {
        var amount = 0
        _applyOrderState.value.menu.forEach { product ->
            amount += product.amountInBasket
        }
        return amount
    }

    fun getDeliveryTime() = _applyOrderState.value.deliveryTime
}