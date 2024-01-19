package com.bav.wbapp.promotions

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bav.core.promotions.PromotionRepository
import com.bav.core.promotions.PromotionRequest
import com.bav.wbapp.auth.PromotionsAction
import com.bav.wbapp.auth.PromotionsState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody

class PromotionViewModel(private val repository: PromotionRepository) : ViewModel() {

    private val _data: MutableStateFlow<PromotionsState> = MutableStateFlow(PromotionsState(isLoading = true))
    val data = _data.asStateFlow()

    fun loadingPromotions(action: PromotionsAction) {
        when (action) {
            is PromotionsAction.LoadingAction -> {
                _data.value = _data.value.copy(
                    isLoading = true,
                    errorMessage = ""
                )
                loading()
            }
        }
    }

    fun savePromotion() {
        val promotionsList = listOf(
            PromotionRequest(
                title = "Акция 1",
                description = "Мы знаем, как поднять вам настроение: все алкогольные коктейли – всего по 299р.!",
                date = "2024-12-03T10:15:30"
            ),
            PromotionRequest(
                title = "Акция 2",
                description = "Мы знаем, как поднять вам настроение: все алкогольные коктейли – всего по 299р.!",
                date = "2024-12-03T10:15:30"
            ),
            PromotionRequest(
                title = "Акция 3",
                description = "Мы знаем, как поднять вам настроение: все алкогольные коктейли – всего по 299р.!",
                date = "2024-12-03T10:15:30"
            ),
            PromotionRequest(
                title = "Акция 4",
                description = "Мы знаем, как поднять вам настроение: все алкогольные коктейли – всего по 299р.!",
                date = "2024-12-03T10:15:30"
            ),
            PromotionRequest(
                title = "Акция 5",
                description = "Мы знаем, как поднять вам настроение: все алкогольные коктейли – всего по 299р.!",
                date = "2024-12-03T10:15:30"
            )
        )

        // Картинки пока что не получается загрузить(
        val requestBody = "test".toRequestBody("text/plain".toMediaTypeOrNull())
        val body = MultipartBody.Part.create(requestBody)

        viewModelScope.launch(Dispatchers.IO) {
            promotionsList.forEach { data ->
                try {
                    repository.save(data, body)
                } catch (e: Exception) {
                    Log.e("SavePromotion", e.message.toString())
                }
            }
        }
    }

    private fun loading() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = repository.getAll()
                withContext(Dispatchers.Main) {
                    _data.value = _data.value.copy(
                        isLoading = false,
                        data = response,
                        errorMessage = ""
                    )
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _data.value = _data.value.copy(
                        isLoading = true,
                        data = emptyList(),
                        errorMessage = e.message.toString()
                    )
                }
            }
        }
    }
}