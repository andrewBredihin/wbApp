package com.bav.wbapp.restaurants

import android.os.Bundle
import android.view.View
import com.bav.core.EmptyScreen

class RestaurantsScreen : EmptyScreen() {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.screenTitle.text = "Restaurants Screen"
    }
}