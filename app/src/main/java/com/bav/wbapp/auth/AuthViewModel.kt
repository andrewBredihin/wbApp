package com.bav.wbapp.auth

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bav.core.api.ResponseCode
import com.bav.core.auth.AuthorizationRepository
import com.bav.core.auth.RequestBody
import com.bav.core.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception

class AuthViewModel(
    private val context: Context,
    private val authRepository: AuthorizationRepository
) : ViewModel() {

    companion object {
        const val MIN_FIELD_LENGTH = 6
        const val PHONE_TEMPLATE = "(8|\\+7 )?(\\d{3}) (\\d{3})-(\\d{2})-(\\d{2})"
    }

    /**
     * Поля для логина
     */

    private val _loginFlow = MutableStateFlow("")
    private val loginFlow: StateFlow<String> get() = _loginFlow

    private val _passwordFlow = MutableStateFlow("")
    private val passwordFlow: StateFlow<String> get() = _passwordFlow

    private val _enableButton = loginFlow.combine(passwordFlow) { email, password ->
        checkEmail(email) && password.length >= MIN_FIELD_LENGTH
    }.stateIn(viewModelScope, SharingStarted.Eagerly, false)
    val enableButton: StateFlow<Boolean> get() = _enableButton

    private val _loginState: MutableStateFlow<AuthState> = MutableStateFlow(AuthState.Default)
    val loginState = _loginState.asStateFlow()

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
    ) { name, phone, email, password, personalData ->
        name.length >= MIN_FIELD_LENGTH
                && checkPhone(phone)
                && checkEmail(email)
                && password.length >= MIN_FIELD_LENGTH
                && personalData
    }.stateIn(viewModelScope, SharingStarted.Eagerly, false)
    val enableRegButton: StateFlow<Boolean> get() = _enableRegButton

    private val _registrationState: MutableStateFlow<AuthState> = MutableStateFlow(AuthState.Default)
    val registrationState = _registrationState.asStateFlow()


    private fun checkEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun checkPhone(phone: String): Boolean {
        val result = PHONE_TEMPLATE.toRegex().find(phone)?.value
        return !result.isNullOrEmpty()
    }

    /**
     * Функции для логина
     */
    fun updateLoginFlow(newValue: String) {
        _loginFlow.value = newValue
    }

    fun updatePasswordFlow(newValue: String) {
        _passwordFlow.value = newValue
    }

    fun login() {
        _loginState.value = AuthState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val request = RequestBody(
                    email = loginFlow.value,
                    password = passwordFlow.value,
                )
                val response = authRepository.login(request = request)
                withContext(Dispatchers.Main) {
                    _loginState.value = if (response.code == ResponseCode.RESPONSE_SUCCESSFUL) {
                        AuthState.Success
                    } else {
                        AuthState.Error(context.getString(R.string.login_error))
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _loginState.value = AuthState.Error("Error")
                }
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

    fun registration() {
        _registrationState.value = AuthState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val request = RequestBody(
                    name = nameRegFlow.value,
                    phone = phoneRegFlow.value,
                    email = loginRegFlow.value,
                    password = passwordRegFlow.value,
                )
                val response = authRepository.register(request = request)
                withContext(Dispatchers.Main) {
                    _registrationState.value = if (response.code == ResponseCode.RESPONSE_SUCCESSFUL) {
                        AuthState.Success
                    } else {
                        AuthState.Error(context.getString(R.string.registration_error))
                    }
                }
            } catch (e: Exception) {
                _registrationState.value = AuthState.Error("Error")
            }
        }
    }
}