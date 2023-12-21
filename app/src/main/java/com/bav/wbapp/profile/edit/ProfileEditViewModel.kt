package com.bav.wbapp.profile.edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bav.core.api.ResponseCode
import com.bav.core.profile.ProfileRepository
import com.bav.core.profile.ProfileRequestBody
import com.bav.wbapp.profile.ProfileDataState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProfileEditViewModel(
    private val repository: ProfileRepository
) : ViewModel() {

    companion object {
        const val MIN_NAME_LENGTH = 2
        const val PHONE_TEMPLATE = "(8|\\+7 )?(\\d{3}) (\\d{3})-(\\d{2})-(\\d{2})"
        const val DATE_TEMPLATE = "([1-9]|[1-2][0-9]|3[0-1])[.|/]([1-9]|1[0-2])[.|/][0-9]{4}"
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

    private val _enableEditProfileButton = combine(
        nameFlow,
        lastNameFlow,
        phoneFlow,
        emailFlow,
        dateFlow
    ) { name, lastName, phone, email, date ->
        name.length >= MIN_NAME_LENGTH
                && lastName.length >= MIN_NAME_LENGTH
                && checkPhone(phone)
                && checkEmail(email)
                && checkDate(date)
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

    fun loadProfile() {
        _profileDataState.value = ProfileDataState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            val response = repository.loadProfile()
            withContext(Dispatchers.Main) {
                _profileDataState.value = when (response.code) {
                    ResponseCode.RESPONSE_SUCCESSFUL -> ProfileDataState.Loaded(response = response.body)

                    else                             -> ProfileDataState.Error
                }
            }
        }
    }

    fun editProfile(callback: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val body = ProfileRequestBody(
                name = "${nameFlow.value} ${lastNameFlow.value}",
                phone = phoneFlow.value,
                email = emailFlow.value,
                birthday = dateFlow.value,
                password = "test@mail.ru"
            )
            repository.updateProfile(body)
            withContext(Dispatchers.Main) {
                callback.invoke()
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
}