package com.bav.wbapp.profile.edit

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bav.core.api.ResponseCode
import com.bav.core.combine
import com.bav.core.profile.ProfileRepository
import com.bav.core.profile.ProfileRequestBody
import com.bav.wbapp.profile.ProfileDataState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate

import java.time.format.DateTimeFormatter

class ProfileEditViewModel(
    private val repository: ProfileRepository
) : ViewModel() {

    companion object {
        const val MIN_NAME_LENGTH = 2
        const val MIN_PASSWORD_LENGTH = 2
        const val PHONE_TEMPLATE = "(8|\\+7 )?(\\d{3}) (\\d{3})-(\\d{2})-(\\d{2})"
        const val DATE_TEMPLATE = "[0-9]{4}-([1-9]|1[0-2])-([1-9]|0[1-9]|[1-2][0-9]|3[0-1])"
    }

    private val _profileDataState: MutableStateFlow<ProfileDataState> = MutableStateFlow(ProfileDataState.Default)
    val profileDataState = _profileDataState.asStateFlow()

    private val _nameFlow = MutableStateFlow("")
    private val nameFlow: StateFlow<String> get() = _nameFlow

    private val _lastNameFlow = MutableStateFlow("")
    private val lastNameFlow: StateFlow<String> get() = _lastNameFlow

    private val _emailFlow = MutableStateFlow("")
    private val emailFlow: StateFlow<String> get() = _emailFlow

    private val _phoneFlow = MutableStateFlow("")
    private val phoneFlow: StateFlow<String> get() = _phoneFlow

    private val _dateFlow = MutableStateFlow("")
    private val dateFlow: StateFlow<String> get() = _dateFlow

    private val _passwordFlow = MutableStateFlow("")
    private val passwordFlow: StateFlow<String> get() = _passwordFlow

    private val _confirmPasswordFlow = MutableStateFlow("")
    private val confirmPasswordFlow: StateFlow<String> get() = _confirmPasswordFlow

    private val _editPasswordCheckFlow = MutableStateFlow(false)
    private val editPasswordCheckFlow: StateFlow<Boolean> get() = _editPasswordCheckFlow

    private val _enableEditProfileButton = combine(
        nameFlow,
        lastNameFlow,
        phoneFlow,
        emailFlow,
        dateFlow,
        passwordFlow,
        confirmPasswordFlow,
        editPasswordCheckFlow
    ) { name, lastName, phone, email, date, password, confirmPassword, passwordCheck ->
        name.length >= MIN_NAME_LENGTH
                && lastName.length >= MIN_NAME_LENGTH
                && checkPhone(phone)
                && checkEmail(email)
                && checkDate(date)
                && checkPassword(password, confirmPassword, passwordCheck)
    }.stateIn(viewModelScope, SharingStarted.Eagerly, false)
    val enableEditProfileButton: StateFlow<Boolean> get() = _enableEditProfileButton

    fun updateNameFlow(newValue: String) {
        _nameFlow.value = newValue
    }

    fun updateLastNameFlow(newValue: String) {
        _lastNameFlow.value = newValue
    }

    fun updateEmailFlow(newValue: String) {
        _emailFlow.value = newValue
    }

    fun updatePhoneFlow(newValue: String) {
        _phoneFlow.value = newValue
    }

    fun updateDateFlow(newValue: String) {
        _dateFlow.value = newValue
    }

    fun updatePasswordFlow(newValue: String) {
        _passwordFlow.value = newValue
    }

    fun updateConfirmPasswordFlow(newValue: String) {
        _confirmPasswordFlow.value = newValue
    }

    fun updateEditPasswordCheckFlow(newValue: Boolean) {
        _editPasswordCheckFlow.value = newValue
    }

    fun loadProfile() {
        _profileDataState.value = ProfileDataState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = repository.loadProfile()
                withContext(Dispatchers.Main) {
                    _profileDataState.value = when (response.code) {
                        ResponseCode.RESPONSE_SUCCESSFUL -> ProfileDataState.Loaded(response = response.body)

                        else                             -> ProfileDataState.Error
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    ProfileDataState.Error
                }
            }
        }
    }

    fun editProfile(callback: () -> Unit) {
        _profileDataState.value = ProfileDataState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val formatter = DateTimeFormatter.ofPattern("yyyy-MM-d")
                val date = LocalDate.parse(dateFlow.value, formatter).toString()
                val body = ProfileRequestBody(
                    name = "${nameFlow.value} ${lastNameFlow.value}",
                    phone = phoneFlow.value,
                    email = emailFlow.value,
                    birthday = date,
                    password = passwordFlow.value
                )
                repository.updateProfile(body)
                withContext(Dispatchers.Main) {
                    callback.invoke()
                }
            } catch (e: Exception) {
                Log.e("editProfile", e.message.toString())
            }
        }
    }

    private fun checkEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun checkPhone(phone: String): Boolean {
        val result = PHONE_TEMPLATE.toRegex().find(phone)?.value
        return !result.isNullOrEmpty()
    }

    private fun checkDate(date: String?): Boolean {
        if (date.isNullOrEmpty()) {
            return false
        }
        val result = DATE_TEMPLATE.toRegex().find(date)?.value
        return !result.isNullOrEmpty()
    }

    private fun checkPassword(
        newPassword: String,
        confirmPassword: String,
        passwordCheck: Boolean
    ): Boolean {
        return if (passwordCheck) {
            if (newPassword.length >= MIN_PASSWORD_LENGTH && confirmPassword.length >= MIN_PASSWORD_LENGTH) {
                newPassword == confirmPassword
            } else {
                false
            }
        } else {
            true
        }
    }
}