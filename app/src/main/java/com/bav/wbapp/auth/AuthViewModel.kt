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

    /**
     * Поля для логина
     */

    private val _loginFlow = MutableStateFlow("")
    private val loginFlow: StateFlow<String> get() = _loginFlow

    private val _passwordFlow = MutableStateFlow("")
    private val passwordFlow: StateFlow<String> get() = _passwordFlow

    private val _enableButton = loginFlow.combine(passwordFlow) { login, password ->
        // TODO() заменить проверку почты
        login.length >= MIN_FIELD_LENGTH && password.length >= MIN_FIELD_LENGTH
    }.stateIn(viewModelScope, SharingStarted.Eagerly, false)
    val enableButton: StateFlow<Boolean> get() = _enableButton

    /**
    * Поля для регистрации
    */
    private val _nameRegFlow = MutableStateFlow("")
    private val nameRegFlow: StateFlow<String> get() = _nameRegFlow

    private val _phoneRegFlow = MutableStateFlow("")
    private val phoneRegFlow: StateFlow<String> get() = _phoneRegFlow

    private val _loginRegFlow = MutableStateFlow("")
    private val loginRegFlow: StateFlow<String> get() = _loginRegFlow

    private val _passwordRegFlow = MutableStateFlow("")
    private val passwordRegFlow: StateFlow<String> get() = _passwordRegFlow

    private val _personalDataRegRegFlow = MutableStateFlow(false)
    private val personalDataRegRegFlow: StateFlow<Boolean> get() = _personalDataRegRegFlow

    private val _enableRegButton = combine(
        nameRegFlow,
        phoneRegFlow,
        loginRegFlow,
        passwordRegFlow,
        personalDataRegRegFlow
    ) { name, phone, login, password, personalData ->
        // TODO() заменить проверку почты и телефона
        name.length >= MIN_FIELD_LENGTH
                && phone.length >= MIN_FIELD_LENGTH
                && login.length >= MIN_FIELD_LENGTH
                && password.length >= MIN_FIELD_LENGTH
                && personalData
    }.stateIn(viewModelScope, SharingStarted.Eagerly, false)
    val enableRegButton: StateFlow<Boolean> get() = _enableRegButton


    /**
     * Функции для логина
     */
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

    /**
     * Функции для регистрации
     */
    fun updateNameRegFlow(newValue: String) {
        _nameRegFlow.value = newValue
    }
    fun updatePhoneRegFlow(newValue: String) {
        _phoneRegFlow.value = newValue
    }
    fun updateLoginRegFlow(newValue: String) {
        _loginRegFlow.value = newValue
    }
    fun updatePasswordRegFlow(newValue: String) {
        _passwordRegFlow.value = newValue
    }

    fun updatePersonalDataCheck(newValue: Boolean) {
        _personalDataRegRegFlow.value = newValue
    }

    fun registration(callback: (ResponseDataModel) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val request = RequestBody(
                name = nameRegFlow.value,
                phone = phoneRegFlow.value,
                email = loginRegFlow.value,
                password = passwordRegFlow.value,
            )
            val response = authRepository.register(request = request)
            withContext(Dispatchers.Main) {
                callback(response)
            }
        }
    }
}