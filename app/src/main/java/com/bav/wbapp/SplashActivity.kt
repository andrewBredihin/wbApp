package com.bav.wbapp

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bav.core.api.TokenManager
import com.bav.core.navigate
import kotlinx.coroutines.launch
import org.koin.android.ext.android.get

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        scheduleSplashScreen()
    }

    private fun scheduleSplashScreen() {
        /** Если токен существует в системе,
         * то переходить сразу на глывный экран
         */
        lifecycleScope.launch {
            val tokenManager: TokenManager = get()
            tokenManager.getToken.collect {
                if (it != null) {
                    this@SplashActivity.navigate(MainActivity::class.java)
                } else {
                    this@SplashActivity.navigate(AuthActivity::class.java)
                }
            }
        }
    }
}