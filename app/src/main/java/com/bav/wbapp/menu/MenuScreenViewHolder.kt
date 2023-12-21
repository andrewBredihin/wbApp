package com.bav.wbapp.menu

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bav.core.EditAmountBinder
import com.bav.core.setupImage
import com.bav.wbapp.databinding.MenuRecyclerHolderBinding
import com.bav.wbapp.menu.model.MenuItemModel

class MenuScreenViewHolder(
    private val binding: MenuRecyclerHolderBinding,
    private val callback: (Int, Int) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    private var _editAmountBinder: EditAmountBinder? = null
    private val editAmountBinder get() = _editAmountBinder!!

    fun bind(model: MenuItemModel) {
        with(binding) {
            _editAmountBinder = EditAmountBinder(
                amount = menuRecyclerHolderEditAmount.amount,
                amountMinus = menuRecyclerHolderEditAmount.amountMinus,
                amountPlus = menuRecyclerHolderEditAmount.amountPlus,
                callback = {
                    menuRecyclerHolderEditAmountContainer.visibility = View.INVISIBLE
                    menuRecyclerHolderButtonPrice.visibility = View.VISIBLE
                }
            )
            editAmountBinder.bind()

            menuRecyclerHolderBackground.setOnClickListener {
                callback(bindingAdapterPosition, editAmountBinder.getAmount())
            }

            menuRecyclerHolderTitle.text = model.title
            menuRecyclerHolderDescription.text = model.description
            // FIXME() цена не приходит, поставил количество
            menuRecyclerHolderButtonPrice.text = model.amount.toString()
            menuRecyclerHolderButtonPrice.setOnClickListener {
                editAmountBinder.setAmount()
                menuRecyclerHolderEditAmountContainer.visibility = View.VISIBLE
                menuRecyclerHolderButtonPrice.visibility = View.INVISIBLE
            }
            // FIXME() картинка тоже не приходит, поставил заглушку
            menuRecyclerHolderBackground.setupImage("https://cdn.zeplin.io/594d116d8ab5d1e677728fa6/assets/F262F850-820A-4BC5-AB96-56253A623752.png")

            EditAmountBinder(
                amount = menuRecyclerHolderEditAmount.amount,
                amountMinus = menuRecyclerHolderEditAmount.amountMinus,
                amountPlus = menuRecyclerHolderEditAmount.amountPlus,
                callback = {
                    menuRecyclerHolderEditAmountContainer.visibility = View.INVISIBLE
                    menuRecyclerHolderButtonPrice.visibility = View.VISIBLE
                }
            )
        }
    }
}