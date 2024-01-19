package com.bav.wbapp

import com.bav.wbapp.auth.login.LoginViewModel
import com.bav.wbapp.auth.registration.RegistrationViewModel
import com.bav.wbapp.main.MainScreenViewModel
import com.bav.wbapp.menu.MenuScreenViewModel
import com.bav.wbapp.profile.ProfileViewModel
import com.bav.wbapp.profile.edit.ProfileEditViewModel
import com.bav.wbapp.promotions.PromotionViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

fun appModule() = module {

    viewModel { MainScreenViewModel() }
    viewModel { ProfileViewModel(get(), get()) }
    viewModel { ProfileEditViewModel(get()) }
    viewModel { MenuScreenViewModel(get()) }
    viewModel { LoginViewModel(get()) }
    viewModel { RegistrationViewModel(get()) }
    viewModel { PromotionViewModel(get()) }
}
