package com.bav.wbapp.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bav.wbapp.R
import com.bav.wbapp.main.model.MainCategoryModel

class MainScreenAdapter : ListAdapter<MainCategoryModel, RecyclerView.ViewHolder>(DiffCallback) {

    companion object {
        private const val BIG_VIEW_TYPE = 1
        private const val SMALL_VIEW_TYPE = 2
    }

    object DiffCallback : DiffUtil.ItemCallback<MainCategoryModel>() {
        override fun areItemsTheSame(oldItem: MainCategoryModel, newItem: MainCategoryModel) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: MainCategoryModel, newItem: MainCategoryModel) =
            oldItem == newItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        when (viewType) {
            BIG_VIEW_TYPE  -> bigCategoryViewHolder(
                LayoutInflater
                    .from(parent.context)
                    .inflate(R.layout.main_recycler_big_holder, parent, false)
            )

            SMALL_VIEW_TYPE -> smallCategoryViewHolder(
                LayoutInflater
                    .from(parent.context)
                    .inflate(R.layout.main_recycler_small_holder, parent, false)
            )

            else            -> error("MainScreenAdapter invalid view type")
        }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is BigCategoryViewHolder -> {
                (getItem(position) as? MainCategoryModel.MainCategoryBig)?.let { model ->
                    holder.bind(model)
                }
            }

            is SmallCategoryViewHolder -> {
                (getItem(position) as? MainCategoryModel.MainCategorySmall)?.let { model ->
                    holder.bind(model)
                }
            }
        }
    }

    override fun getItemViewType(position: Int): Int = when (getItem(position)) {
        is MainCategoryModel.MainCategoryBig    -> BIG_VIEW_TYPE
        else                                    -> SMALL_VIEW_TYPE
    }
}