package com.bav.wbapp.auth

sealed class AuthState {
    data object Default: AuthState()
    data object Loading: AuthState()
    data object Success: AuthState()
    data class Error(val message: String): AuthState()
}
