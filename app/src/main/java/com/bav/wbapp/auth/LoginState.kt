package com.bav.wbapp.auth

sealed class LoginState {
    data object Default: LoginState()
    data object Loading: LoginState()
    data object Login: LoginState()
    data class Error(val message: String): LoginState()
}
