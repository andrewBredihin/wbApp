package com.bav.wbapp

import com.bav.wbapp.auth.AuthViewModel
import com.bav.wbapp.main.MainScreenViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

fun appModule() = module {

    viewModel { MainScreenViewModel() }
    viewModel { AuthViewModel(get(), get()) }
}
