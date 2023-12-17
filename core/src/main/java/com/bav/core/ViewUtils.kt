package com.bav.core

import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import com.bumptech.glide.Glide

fun ImageView.setupImage(url: String) {
    Glide.with(this.context)
        .asBitmap()
        .load(url)
        .skipMemoryCache(true)
        .into(this)
}

fun Fragment.getNavController() =
    (requireActivity() as Navigation).getNavController()

fun Fragment.navigate(action: NavDirections) =
    getNavController().navigate(action)