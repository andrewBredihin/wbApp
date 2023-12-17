package com.bav.wbapp.profile

import android.os.Bundle
import android.view.View
import com.bav.core.EmptyScreen

class ProfileScreen : EmptyScreen() {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.screenTitle.text = "Profile Screen"
    }
}