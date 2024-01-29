package com.bav.wbapp

import android.app.Application
import com.yandex.mapkit.MapKitFactory
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        MapKitFactory.setApiKey(BuildConfig.MAP_API_KEY)
        startKoin {
            androidLogger()
            androidContext(this@App)
            modules(
                appModule(),
                networkModule()
            )
        }
    }
}