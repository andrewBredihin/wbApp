package com.bav.core

import android.app.Activity
import android.content.Intent
import android.view.View
import android.widget.ImageView
import androidx.core.graphics.drawable.DrawableCompat
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import com.bumptech.glide.Glide
import kotlinx.coroutines.flow.Flow

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
    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
    finish()
}

fun <T1, T2, T3, T4, T5, T6, T7, T8, R> combine(
    flow: Flow<T1>,
    flow2: Flow<T2>,
    flow3: Flow<T3>,
    flow4: Flow<T4>,
    flow5: Flow<T5>,
    flow6: Flow<T6>,
    flow7: Flow<T7>,
    flow8: Flow<T8>,
    transform: suspend (T1, T2, T3, T4, T5, T6, T7, T8) -> R
): Flow<R> = kotlinx.coroutines.flow.combine(flow, flow2, flow3, flow4, flow5, flow6, flow7, flow8) { args: Array<*> ->
    transform(
        args[0] as T1,
        args[1] as T2,
        args[2] as T3,
        args[3] as T4,
        args[4] as T5,
        args[5] as T6,
        args[6] as T7,
        args[7] as T8
    )
}