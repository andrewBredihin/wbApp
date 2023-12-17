package com.bav.wbapp.info

import android.os.Bundle
import android.view.View
import com.bav.core.EmptyScreen

class InfoScreen : EmptyScreen() {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.screenTitle.text = "Info Screen"
    }
}