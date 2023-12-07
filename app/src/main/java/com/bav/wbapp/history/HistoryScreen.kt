package com.bav.wbapp.history

import android.os.Bundle
import android.view.View
import com.bav.core.EmptyScreen

class HistoryScreen : EmptyScreen() {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.screenTitle.text = "History Screen"
    }
}