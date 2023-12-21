package com.bav.wbapp

import com.bav.wbapp.auth.AuthViewModel
import com.bav.wbapp.main.MainScreenViewModel
import com.bav.wbapp.menu.MenuScreenViewModel
import com.bav.wbapp.profile.ProfileViewModel
import com.bav.wbapp.profile.edit.ProfileEditViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

fun appModule() = module {

    viewModel { MainScreenViewModel() }
    viewModel { AuthViewModel(get()) }
    viewModel { ProfileViewModel(get(), get()) }
    viewModel { ProfileEditViewModel(get()) }
    viewModel { MenuScreenViewModel(get()) }
}
