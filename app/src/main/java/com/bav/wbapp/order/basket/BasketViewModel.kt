package com.bav.wbapp.order.basket

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bav.core.basket.ProductDao
import com.bav.core.basket.ProductEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception

class BasketViewModel(private val productDao: ProductDao) : ViewModel() {

    companion object {
        const val PROMO_CODE_TEMPLATE = "(\\d{4})-(\\d{4})-(\\d{4})"

        const val PROMO_CODE_ENTER = "Введите промокод"
        const val PROMO_CODE_ACTIVE = "Промокод применен"
        const val PROMO_CODE_ERROR = "Промокод введен не верно"
    }

    private val _basketState: MutableStateFlow<BasketState> = MutableStateFlow(BasketState(isLoading = true, promoCodeMessage = PROMO_CODE_ENTER))
    val basketState = _basketState.asStateFlow()

    fun startAction(action: BasketAction) {
        when (action) {
            is BasketAction.LoadingAction     -> {
                loadBasket()
            }

            is BasketAction.ActivatePromoCode -> {
                val code = action.code
                if (code.isNotEmpty()) {
                    val checkPromoCode: Boolean = !PROMO_CODE_TEMPLATE.toRegex().find(code)?.value.isNullOrEmpty()
                    if (checkPromoCode) {
                        _basketState.value = _basketState.value.copy(
                            promoCode = code,
                            promoCodeStatus = PromoCodeStatus.ACTIVE,
                            promoCodeMessage = PROMO_CODE_ACTIVE
                        )
                    } else {
                        _basketState.value = _basketState.value.copy(
                            promoCodeMessage = PROMO_CODE_ERROR,
                            promoCodeStatus = PromoCodeStatus.ERROR
                        )
                    }
                } else {
                    _basketState.value = _basketState.value.copy(
                        promoCodeMessage = PROMO_CODE_ENTER,
                        promoCodeStatus = PromoCodeStatus.ENTER
                    )
                }
            }

            is BasketAction.SetGuestsAmount   -> {
                _basketState.value = _basketState.value.copy(
                    guestsAmount = action.amount
                )
            }
        }
    }

    /** Загрузка из корзины */
    private fun loadBasket() {
        _basketState.value = _basketState.value.copy(isLoading = true)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                delay(1000)
                val list = productDao.getAll()
                withContext(Dispatchers.Main) {
                    val price = calculatePrice(list)
                    _basketState.value = _basketState.value.copy(
                        isLoading = false,
                        data = list,
                        price = price
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
            productDao.updateByProductId(productId, amount)
            withContext(Dispatchers.Main) {
                val list = mutableListOf<ProductEntity>()
                _basketState.value.data.forEach {
                    if (it.productId != productId) {
                        list.add(it)
                    } else {
                        list.add(it.copy(amountInBasket = amount))
                    }
                }
                val price = calculatePrice(list)
                _basketState.value = _basketState.value.copy(
                    isLoading = false,
                    data = list.toList(),
                    price = price
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
            productDao.deleteByProductId(productId)
            val list = _basketState.value.data.filter { it.productId != productId }
            withContext(Dispatchers.Main) {
                if (list.isNotEmpty()) {
                    val price = calculatePrice(list)
                    _basketState.value = _basketState.value.copy(
                        isLoading = false,
                        data = list,
                        price = price
                    )
                } else {
                    loadBasket()
                }
            }
        }
    }

    private fun calculatePrice(products: List<ProductEntity> = _basketState.value.data): Int {
        var price = 0f
        val discountValue = _basketState.value.discount
        products.forEach { product ->
            price += product.price * product.amountInBasket
        }
        price *= (100f - discountValue) / 100f
        return price.toInt()
    }

    fun getPrice() = _basketState.value.price
    fun getGuestsAmount() = _basketState.value.guestsAmount + 1
}