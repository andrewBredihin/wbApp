package com.bav.wbapp.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bav.core.auth.AuthorizationRepository
import com.bav.core.auth.RequestBody
import com.bav.core.auth.ResponseDataModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AuthViewModel(private val authRepository: AuthorizationRepository) : ViewModel() {

    companion object {
        const val MIN_FIELD_LENGTH = 6
    }

    private val _loginFlow = MutableStateFlow("")
    private val loginFlow: StateFlow<String> get() = _loginFlow

    private val _passwordFlow = MutableStateFlow("")
    private val passwordFlow: StateFlow<String> get() = _passwordFlow

    private val _enableButton = loginFlow.combine(passwordFlow) { login, password ->
        login.length >= MIN_FIELD_LENGTH && password.length >= MIN_FIELD_LENGTH
    }.stateIn(viewModelScope, SharingStarted.Eagerly, false)
    val enableButton: StateFlow<Boolean> get() = _enableButton


    fun updateLoginFlow(newValue: String) {
        _loginFlow.value = newValue
    }
    fun updatePasswordFlow(newValue: String) {
        _passwordFlow.value = newValue
    }

    fun login(callback: (ResponseDataModel) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val request = RequestBody(
                email = loginFlow.value,
                password = passwordFlow.value,
            )
            val response = authRepository.login(request = request)
            withContext(Dispatchers.Main) {
                callback(response)
            }
        }
    }
}