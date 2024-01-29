package com.bav.wbapp.restaurants

import androidx.lifecycle.ViewModel
import com.yandex.mapkit.geometry.Point
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class RestaurantsMapViewModel : ViewModel() {

    private val _currentRestaurant: MutableStateFlow<RestaurantInfo?> = MutableStateFlow(null)
    val currentRestaurant = _currentRestaurant.asStateFlow()

    private val _restaurants: MutableStateFlow<List<RestaurantInfo>> = MutableStateFlow(emptyList())
    val restaurants = _restaurants.asStateFlow()

    init {
        _restaurants.value = listOf(
            RestaurantInfo(
                id = 0,
                title = "Нияма и Пицца-Пи",
                area = "Мытищи",
                address = "Шараповский пр-д, 2, ТРЦ «Крсный кит», 3-й этаж",
                point = Point(55.840245, 37.492024)
            ),
            RestaurantInfo(
                id = 1,
                title = "Нияма и Пицца-Пи",
                area = "Водный стадион",
                address = "Головинсое шоссе, 5, к 1",
                point = Point(55.916678, 37.759264)
            ),
            RestaurantInfo(
                id = 2,
                title = "Нияма и Пицца-Пи",
                area = "Адмирала Ушакова",
                address = "Адмирала Лазарева, 2, ТЦ «Виктория», 3-й этаж",
                point = Point(55.547036, 37.543237)
            )
        )
    }

    fun getRestaurants() = _restaurants.value

    fun setRestaurant(restaurant: RestaurantInfo) {
        _currentRestaurant.value = restaurant
    }
}