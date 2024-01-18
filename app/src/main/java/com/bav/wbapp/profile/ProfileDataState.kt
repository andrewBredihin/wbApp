package com.bav.wbapp.profile

import com.bav.core.profile.ProfileResponseDataModel

sealed class ProfileDataState {
    data object Default : ProfileDataState()
    data object Loading : ProfileDataState()
    data object Error : ProfileDataState()
    class Loaded(val response: ProfileResponseDataModel?) : ProfileDataState()
}
