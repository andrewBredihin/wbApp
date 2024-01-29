package com.bav.wbapp.order.basket

import androidx.recyclerview.widget.RecyclerView
import com.bav.core.basket.ProductEntity
import com.bav.core.setupImage
import com.bav.wbapp.databinding.MenuRecyclerHolderBinding

class BasketScreenViewHolder(
    private val binding: MenuRecyclerHolderBinding,
    private val callback: (Int, Int) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    companion object {
        private const val NOT_AVAILABLE = "Товар не доступен"
    }

    fun bind(model: ProductEntity) {
        with(binding) {
            menuRecyclerHolderTitle.text = model.title
            menuRecyclerHolderDescription.text = if (model.available) {
                priceText(model.price, model.amountInBasket)
            } else {
                NOT_AVAILABLE
            }
            /**
             * Максимальное количество товара в корзине - количество товара, которое приходит из сети (amount)
             * Текущее количество товара в корзине берется из БД
             * callback возвращает актуальное количество товара в корзине
             *  */
            amountView.apply {
                setMinAmount(0)
                setMaxAmount(model.amount)
                setText("")
                setCurrentAmount(model.amountInBasket)
                setEditAmountListener { amount ->
                    menuRecyclerHolderDescription.text = priceText(model.price, amount)
                    callback(bindingAdapterPosition, amount)
                }
            }
            menuRecyclerHolderBackground.setupImage("https://cdn.zeplin.io/594d116d8ab5d1e677728fa6/assets/C8F2D97A-53B0-4CC0-8B0A-27F45D50399C.png")
        }
    }

    /** Рассчет цены по количествву товара и его цены*/
    private fun priceText(price: Float, amount: Int) = "$price P x $amount = ${price * amount}"
}