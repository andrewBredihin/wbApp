package com.bav.wbapp.menu

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bav.wbapp.basket.ProductInBasket
import com.bav.wbapp.databinding.MenuRecyclerHolderBinding
import com.bav.wbapp.menu.model.MenuItemModel

class MenuScreenAdapter(private val callback: (Int, Int) -> Unit) :
    ListAdapter<MenuItemModel, RecyclerView.ViewHolder>(DiffCallback) {

    object DiffCallback : DiffUtil.ItemCallback<MenuItemModel>() {
        override fun areItemsTheSame(oldItem: MenuItemModel, newItem: MenuItemModel) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: MenuItemModel, newItem: MenuItemModel) =
            oldItem == newItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        MenuScreenViewHolder(
            binding = MenuRecyclerHolderBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            callback = callback
        )

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as MenuScreenViewHolder).bind(getItem(position))
    }

    fun getProduct(position: Int): ProductInBasket {
        val value = currentList[position]
        return ProductInBasket(
            productId = value.id,
            amount = value.amount,
            title = value.title,
            type = value.type,
            price = 100f
        )
    }
}