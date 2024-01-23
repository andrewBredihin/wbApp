package com.bav.wbapp.basket

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bav.core.basket.AppDatabase
import com.bav.core.basket.ProductEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception

class BasketViewModel(private val db: AppDatabase) : ViewModel() {

    private val _basketState: MutableStateFlow<BasketState> = MutableStateFlow(BasketState(isLoading = true))
    val basketState = _basketState.asStateFlow()

    fun startAction(action: BasketAction) {
        when (action) {
            is BasketAction.LoadingAction -> {
                loadBasket()
            }
        }
    }

    /** Загрузка из корзины */
    private fun loadBasket() {
        _basketState.value = _basketState.value.copy(isLoading = true)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                delay(1000)
                val list = db.productDao().getAll()
                withContext(Dispatchers.Main) {
                    _basketState.value = _basketState.value.copy(
                        isLoading = false,
                        data = list
                    )
                }
            } catch (e: Exception) {
                Log.e("DataBase", e.message.toString())
            }
        }
    }

    /** Обновление БД при изменении количества */
    fun updateProductInBasket(productId: Int, amount: Int) {
        if (amount == 0) {
            deleteProduct(productId)
        } else {
            updateProduct(productId, amount)
        }
    }

    /** Изменить количество товара в корзине и обновить state для отображения актуальной цены */
    private fun updateProduct(productId: Int, amount: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            db.productDao().updateByProductId(productId, amount)
            withContext(Dispatchers.Main) {
                val list = mutableListOf<ProductEntity>()
                _basketState.value.data.forEach {
                    if (it.productId != productId) {
                        list.add(it)
                    } else {
                        list.add(it.copy(amountInBasket = amount))
                    }
                }
                _basketState.value = _basketState.value.copy(
                    isLoading = false,
                    data = list.toList()
                )
            }
        }
    }

    /**
     * Удалить товар из корзины и обновить state для отображения актуальной цены.
     * Если корзина пуста - запуск loadBasket() -> на экране перестанет отображаться информация о заказе, т.к. его нет
     * */
    private fun deleteProduct(productId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            db.productDao().deleteByProductId(productId)
            val list = _basketState.value.data.filter { it.productId != productId }
            withContext(Dispatchers.Main) {
                if (list.isNotEmpty()) {
                    _basketState.value = _basketState.value.copy(
                        isLoading = false,
                        data = list
                    )
                } else {
                    loadBasket()
                }
            }
        }
    }
}