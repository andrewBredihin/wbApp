package com.bav.wbapp.restaurants

import com.yandex.mapkit.geometry.Point

data class RestaurantInfo(
    val id: Int,
    val title: String,
    val area: String,
    val address: String,
    val point: Point
)