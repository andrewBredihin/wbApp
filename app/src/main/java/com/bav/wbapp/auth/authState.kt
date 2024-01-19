package com.bav.wbapp.auth

sealed class LoginAction {
    class UpdateEmailAction(val email: String) : LoginAction()
    class UpdatePasswordAction(val password: String) : LoginAction()
    data object LoadingAction : LoginAction()
}

data class LoginState(
    val email: String = "",
    val password: String = "",
    val errorMessage: String = "",
    val isLoading: Boolean = false,
    val isEnabled: Boolean = false,
    val isAuth: Boolean = false
)

sealed class RegistrationAction {
    class UpdateNameAction(val name: String) : RegistrationAction()
    class UpdatePhoneAction(val phone: String) : RegistrationAction()
    class UpdateEmailAction(val email: String) : RegistrationAction()
    class UpdatePasswordAction(val password: String) : RegistrationAction()
    class UpdateCheckAction(val check: Boolean) : RegistrationAction()
    data object LoadingAction : RegistrationAction()
}

data class RegistrationState(
    val name: String = "",
    val phone: String = "",
    val email: String = "",
    val password: String = "",
    val errorMessage: String = "",
    val isCheck: Boolean = false,
    val isLoading: Boolean = false,
    val isEnabled: Boolean = false,
    val isRegistration: Boolean = false
)
