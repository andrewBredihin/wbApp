package com.bav.wbapp.profile

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bav.core.api.ResponseCode
import com.bav.core.auth.AuthorizationRepository
import com.bav.core.profile.ProfileRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File


class ProfileViewModel(
    private val repository: ProfileRepository,
    private val authRepository: AuthorizationRepository
) : ViewModel() {

    private val _profileDataState: MutableStateFlow<ProfileDataState> = MutableStateFlow(ProfileDataState.Default)
    val profileDataState = _profileDataState.asStateFlow()

    fun loadProfile() {
        _profileDataState.value = ProfileDataState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = repository.loadProfile()
                val result = repository.loadAvatar()
                withContext(Dispatchers.Main) {
                    _profileDataState.value = when (response.code) {
                        ResponseCode.RESPONSE_SUCCESSFUL -> {
                            val body = response.body?.copy(imageUrl = result.image)
                            ProfileDataState.Loaded(response = body)
                        }

                        else                             -> ProfileDataState.Error
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _profileDataState.value = ProfileDataState.Error
                }
            }
        }
    }

    fun logout(callback: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                authRepository.logout()
                withContext(Dispatchers.Main) {
                    callback.invoke()
                }
            } catch (e: Exception) {
                Log.e("logout", e.cause.toString())
            }
        }
    }

    fun uploadAvatar(path: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val file = File(path)
            val requestBody = file.asRequestBody("image/*".toMediaTypeOrNull())
            val body = MultipartBody.Part.createFormData("file", file.name, requestBody)
            val response = repository.uploadAvatar(body)
            if(response.code() == ResponseCode.RESPONSE_SUCCESSFUL) {
                loadProfile()
            }
        }
    }
}