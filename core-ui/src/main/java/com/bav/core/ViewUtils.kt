package com.bav.core

import android.widget.ImageView
import com.bumptech.glide.Glide

fun ImageView.setupImage(url: String) {
    Glide.with(this.context)
        .asBitmap()
        .load(url)
        .skipMemoryCache(true)
        .into(this)
}