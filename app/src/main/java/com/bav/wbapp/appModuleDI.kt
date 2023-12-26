package com.bav.wbapp

import com.bav.wbapp.auth.login.LoginViewModel
import com.bav.wbapp.auth.registration.RegistrationViewModel
import com.bav.wbapp.main.MainScreenViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

fun appModule() = module {

    viewModel { MainScreenViewModel() }
    viewModel { LoginViewModel(get()) }
    viewModel { RegistrationViewModel(get()) }
}
