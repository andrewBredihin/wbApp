package com.bav.wbapp.stock

import android.os.Bundle
import android.view.View
import com.bav.core.EmptyScreen

class StockScreen : EmptyScreen() {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.screenTitle.text = "Stock Screen"
    }
}