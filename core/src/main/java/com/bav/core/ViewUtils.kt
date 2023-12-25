package com.bav.core

import android.app.Activity
import android.content.Intent
import android.view.View
import android.widget.ImageView
import androidx.core.graphics.drawable.DrawableCompat
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

fun View.setTintColor(color: Int) {
    val wrappedDrawable = DrawableCompat.wrap(this.background)
    DrawableCompat.setTint(wrappedDrawable.mutate(), color)
    this.background = wrappedDrawable
}

fun Activity.navigate(activity: Class<out Activity>) {
    val intent = Intent(this, activity)
    startActivity(intent)
    finish()
}