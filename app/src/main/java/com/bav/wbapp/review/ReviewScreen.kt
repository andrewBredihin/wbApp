package com.bav.wbapp.review

import android.os.Bundle
import android.view.View
import com.bav.core.EmptyScreen

class ReviewScreen : EmptyScreen() {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.screenTitle.text = "Review Screen"
    }
}