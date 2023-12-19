package com.bav.wbapp.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bav.core.api.ResponseCode
import com.bav.core.profile.ProfileRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProfileViewModel(private val repository: ProfileRepository) : ViewModel() {

    private val _profileDataState: MutableStateFlow<ProfileDataState> = MutableStateFlow(ProfileDataState.Default)
    val profileDataState = _profileDataState.asStateFlow()

    fun loadProfile() {
        _profileDataState.value = ProfileDataState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            val response = repository.loadProfile()
            withContext(Dispatchers.Main) {
                _profileDataState.value = when(response.code) {
                    ResponseCode.RESPONSE_SUCCESSFUL    -> ProfileDataState.Loaded(response = response.body)

                    else                                -> ProfileDataState.Error
                }
            }
        }
    }
}