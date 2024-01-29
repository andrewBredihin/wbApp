package com.bav.wbapp

import androidx.room.Room
import com.bav.core.basket.AppDatabase
import com.bav.wbapp.auth.login.LoginViewModel
import com.bav.wbapp.auth.registration.RegistrationViewModel
import com.bav.wbapp.basket.BasketViewModel
import com.bav.wbapp.main.MainScreenViewModel
import com.bav.wbapp.menu.MenuScreenViewModel
import com.bav.wbapp.order.apply.ApplyOrderViewModel
import com.bav.wbapp.order.create.OrderViewModel
import com.bav.wbapp.profile.ProfileViewModel
import com.bav.wbapp.profile.edit.ProfileEditViewModel
import com.bav.wbapp.promotions.PromotionViewModel
import com.bav.wbapp.restaurants.RestaurantsMapViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

fun appModule() = module {

    viewModel { MainScreenViewModel() }
    viewModel { ProfileViewModel(get(), get()) }
    viewModel { ProfileEditViewModel(get()) }
    viewModel { MenuScreenViewModel(get(), get()) }
    viewModel { LoginViewModel(get()) }
    viewModel { RegistrationViewModel(get()) }
    viewModel { PromotionViewModel(get()) }
    viewModel { BasketViewModel(get()) }
    viewModel { OrderViewModel(get()) }
    viewModel { ApplyOrderViewModel(get()) }
    viewModel { RestaurantsMapViewModel() }

    single {
        Room.databaseBuilder(
            get(),
            AppDatabase::class.java, "basket-database"
        ).build()
    }
}
