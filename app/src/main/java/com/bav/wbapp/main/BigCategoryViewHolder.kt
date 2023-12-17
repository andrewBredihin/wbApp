package com.bav.wbapp.main

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bav.core.setupImage
import com.bav.wbapp.R
import com.bav.wbapp.main.model.MainCategoryModel

class BigCategoryViewHolder(
    view: View,
    backgroundImage: ImageView,
    title: TextView,
    callback: (Int) -> Unit
) : MainScreenViewHolder(view, backgroundImage, title, callback){

    fun bind(model: MainCategoryModel.MainCategoryBig) {
        backgroundImage.setupImage(model.imageUrl)
        title.text = model.title
    }
}

fun bigCategoryViewHolder(view: View, callback: (Int) -> Unit) =
    BigCategoryViewHolder(
        view,
        backgroundImage = view.findViewById(R.id.main_recycler_big_holder_background),
        title = view.findViewById(R.id.main_recycler_big_holder_title),
        callback = callback
    )