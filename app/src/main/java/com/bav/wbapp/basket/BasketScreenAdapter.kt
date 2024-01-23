package com.bav.wbapp.basket

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bav.core.basket.ProductEntity
import com.bav.wbapp.databinding.MenuRecyclerHolderBinding

class BasketScreenAdapter(private val callback: (Int, Int) -> Unit) :
    ListAdapter<ProductEntity, RecyclerView.ViewHolder>(DiffCallback) {

    object DiffCallback : DiffUtil.ItemCallback<ProductEntity>() {
        override fun areItemsTheSame(oldItem: ProductEntity, newItem: ProductEntity) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: ProductEntity, newItem: ProductEntity) =
            oldItem == newItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        BasketScreenViewHolder(
            binding = MenuRecyclerHolderBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            callback = callback
        )

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as BasketScreenViewHolder).bind(getItem(position))
    }

    fun getProductId(position: Int) = currentList[position].productId
}