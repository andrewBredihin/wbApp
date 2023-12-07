package com.bav.wbapp.main

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bav.core.setupImage
import com.bav.wbapp.R
import com.bav.wbapp.main.model.MainCategoryModel

class SmallCategoryViewHolder(view: View, val backgroundImage: ImageView, val title: TextView) :
    RecyclerView.ViewHolder(view) {

    fun bind(model: MainCategoryModel.MainCategorySmall) {
        backgroundImage.setupImage(model.imageUrl)
        title.text = model.title
    }
}

fun smallCategoryViewHolder(view: View) =
    SmallCategoryViewHolder(
        view,
        backgroundImage = view.findViewById(R.id.main_recycler_small_holder_background),
        title = view.findViewById(R.id.main_recycler_small_holder_title)
    )