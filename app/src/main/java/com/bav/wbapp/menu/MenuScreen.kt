package com.bav.wbapp.menu

import android.os.Bundle
import android.view.View
import com.bav.core.EmptyScreen

class MenuScreen : EmptyScreen() {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.screenTitle.text = "Menu Screen"
    }
}