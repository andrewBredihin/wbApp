package com.bav.wbapp.delivery

import android.os.Bundle
import android.view.View
import com.bav.core.EmptyScreen

class DeliveryScreen : EmptyScreen() {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.screenTitle.text = "Delivery Screen"
    }
}