package com.bav.wbapp.auth.login

import androidx.core.util.PatternsCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bav.core.api.ResponseCode
import com.bav.core.auth.AuthorizationRepository
import com.bav.core.auth.RequestBody
import com.bav.wbapp.auth.LoginAction
import com.bav.wbapp.auth.LoginState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginViewModel(private val authRepository: AuthorizationRepository) : ViewModel() {

    companion object {
        const val ERROR_MESSAGE = "Error"
        const val LOGIN_ERROR_MESSAGE = "Неверный логин и/или пароль"
        const val MIN_FIELD_LENGTH = 6
    }

    private val _loginState: MutableStateFlow<LoginState> = MutableStateFlow(LoginState())
    val loginState = _loginState.asStateFlow()

    fun updateLoginState(action: LoginAction) {
        when (action) {
            is LoginAction.LoadingAction        -> {
                _loginState.value = _loginState.value.copy(
                    isLoading = true
                )
                login()
            }

            is LoginAction.UpdateEmailAction    -> {
                _loginState.value = _loginState.value.copy(
                    email = action.email,
                    errorMessage = "",
                    isEnabled = checkLoginFields(
                        email = action.email,
                        password = _loginState.value.password
                    )
                )
            }

            is LoginAction.UpdatePasswordAction -> {
                _loginState.value = _loginState.value.copy(
                    password = action.password,
                    errorMessage = "",
                    isEnabled = checkLoginFields(
                        email = _loginState.value.email,
                        password = action.password
                    )
                )
            }
        }
    }

    private fun login() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val request = RequestBody(
                    email = _loginState.value.email,
                    password = _loginState.value.password,
                )
                val response = authRepository.login(request = request)
                withContext(Dispatchers.Main) {
                    _loginState.value = if (response.code == ResponseCode.RESPONSE_SUCCESSFUL) {
                        _loginState.value.copy(
                            isAuth = true
                        )
                    } else {
                        _loginState.value.copy(
                            errorMessage = LOGIN_ERROR_MESSAGE,
                            isLoading = false
                        )
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _loginState.value = _loginState.value.copy(
                        errorMessage = ERROR_MESSAGE,
                        isLoading = false
                    )
                }
            }
        }
    }

    private fun checkLoginFields(email: String, password: String): Boolean {
        return PatternsCompat.EMAIL_ADDRESS.matcher(email).matches()
                && password.length >= MIN_FIELD_LENGTH
    }
}