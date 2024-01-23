package com.bav.wbapp.menu

import androidx.recyclerview.widget.RecyclerView
import com.bav.core.setupImage
import com.bav.wbapp.databinding.MenuRecyclerHolderBinding
import com.bav.wbapp.menu.model.MenuItemModel

class MenuScreenViewHolder(
    private val binding: MenuRecyclerHolderBinding,
    private val callback: (Int, Int) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(model: MenuItemModel) {
        with(binding) {
            menuRecyclerHolderTitle.text = model.title
            menuRecyclerHolderDescription.text = model.description
            amountView.apply {
                setMinAmount(0)
                setMaxAmount(100)
                setCurrentAmount(model.amountInBasket)
                setText(model.price.toString())
                setEditAmountListener { amount ->
                    callback(bindingAdapterPosition, amount)
                }
            }
            // FIXME() картинка тоже не приходит, поставил заглушку
            menuRecyclerHolderBackground.setupImage("https://cdn.zeplin.io/594d116d8ab5d1e677728fa6/assets/C8F2D97A-53B0-4CC0-8B0A-27F45D50399C.png")
        }
    }
}