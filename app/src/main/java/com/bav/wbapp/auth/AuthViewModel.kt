package com.bav.wbapp.auth

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bav.core.R
import com.bav.core.api.ResponseCode
import com.bav.core.auth.AuthorizationRepository
import com.bav.core.auth.RequestBody
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
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

    private val _loginState: MutableStateFlow<LoginState> = MutableStateFlow(LoginState())
    val loginState = _loginState.asStateFlow()

    private val _registrationState: MutableStateFlow<RegistrationState> = MutableStateFlow(RegistrationState())
    val registrationState = _registrationState.asStateFlow()

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

    fun updateRegistrationState(action: RegistrationAction) {
        when (action) {
            RegistrationAction.LoadingAction           -> {
                _registrationState.value = _registrationState.value.copy(
                    isLoading = true
                )
                registration()
            }

            is RegistrationAction.UpdateCheckAction    -> {
                _registrationState.value = _registrationState.value.copy(
                    isCheck = action.check,
                    errorMessage = "",
                    isEnabled = checkRegistrationFields(
                        name = _registrationState.value.name,
                        phone = _registrationState.value.phone,
                        email = _registrationState.value.email,
                        password = _registrationState.value.password,
                        isCheck = action.check
                    )
                )
            }

            is RegistrationAction.UpdateEmailAction    -> {
                _registrationState.value = _registrationState.value.copy(
                    email = action.email,
                    errorMessage = "",
                    isEnabled = checkRegistrationFields(
                        name = _registrationState.value.name,
                        phone = _registrationState.value.phone,
                        email = action.email,
                        password = _registrationState.value.password,
                        isCheck = _registrationState.value.isCheck
                    )
                )
            }

            is RegistrationAction.UpdateNameAction     -> {
                _registrationState.value = _registrationState.value.copy(
                    name = action.name,
                    errorMessage = "",
                    isEnabled = checkRegistrationFields(
                        name = action.name,
                        phone = _registrationState.value.phone,
                        email = _registrationState.value.email,
                        password = _registrationState.value.password,
                        isCheck = _registrationState.value.isCheck
                    )
                )
            }

            is RegistrationAction.UpdatePasswordAction -> {
                _registrationState.value = _registrationState.value.copy(
                    password = action.password,
                    errorMessage = "",
                    isEnabled = checkRegistrationFields(
                        name = _registrationState.value.name,
                        phone = _registrationState.value.phone,
                        email = _registrationState.value.email,
                        password = action.password,
                        isCheck = _registrationState.value.isCheck
                    )
                )
            }

            is RegistrationAction.UpdatePhoneAction    -> {
                _registrationState.value = _registrationState.value.copy(
                    phone = action.phone,
                    errorMessage = "",
                    isEnabled = checkRegistrationFields(
                        name = _registrationState.value.name,
                        phone = action.phone,
                        email = _registrationState.value.email,
                        password = _registrationState.value.password,
                        isCheck = _registrationState.value.isCheck
                    )
                )
            }
        }
    }

    fun defaultStates() {
        _loginState.value = LoginState()
        _registrationState.value = RegistrationState()
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
                            errorMessage = context.getString(R.string.login_error),
                            isLoading = false
                        )
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _loginState.value = _loginState.value.copy(
                        errorMessage = "Error",
                        isLoading = false
                    )
                }
            }
        }
    }

    private fun registration() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val request = RequestBody(
                    name = _registrationState.value.name,
                    phone = _registrationState.value.phone,
                    email = _registrationState.value.email,
                    password = _registrationState.value.password,
                )
                val response = authRepository.register(request = request)
                withContext(Dispatchers.Main) {
                    _registrationState.value = if (response.code == ResponseCode.RESPONSE_SUCCESSFUL) {
                        _registrationState.value.copy(
                            isRegistration = true
                        )
                    } else {
                        _registrationState.value.copy(
                            errorMessage = context.getString(R.string.registration_error),
                            isLoading = false
                        )
                    }
                }
            } catch (e: Exception) {
                _registrationState.value = _registrationState.value.copy(
                    errorMessage = "Error",
                    isLoading = false
                )
            }
        }
    }

    private fun checkLoginFields(
        email: String,
        password: String
    ): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
                && password.length >= MIN_FIELD_LENGTH
    }

    private fun checkRegistrationFields(
        name: String,
        phone: String,
        email: String,
        password: String,
        isCheck: Boolean
    ): Boolean {
        return name.length >= MIN_FIELD_LENGTH
                && checkPhone(phone)
                && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
                && password.length >= MIN_FIELD_LENGTH
                && isCheck
    }

    private fun checkPhone(phone: String): Boolean {
        val result = PHONE_TEMPLATE.toRegex().find(phone)?.value
        return !result.isNullOrEmpty()
    }
}