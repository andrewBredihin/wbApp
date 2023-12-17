package com.bav.wbapp.main

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

abstract class MainScreenViewHolder(
    view: View,
    val backgroundImage: ImageView,
    val title: TextView,
    val callback: (Int) -> Unit
) : RecyclerView.ViewHolder(view) {

    init {
        view.setOnClickListener {
            callback(bindingAdapterPosition)
        }
    }
}