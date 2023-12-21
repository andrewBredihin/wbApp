package com.bav.wbapp.menu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bav.core.api.ResponseCode
import com.bav.core.menu.MenuRepository
import com.bav.wbapp.menu.model.MenuItemModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MenuScreenViewModel(private val repository: MenuRepository) : ViewModel() {

    private var _menuDataState: MutableStateFlow<MenuDataState> = MutableStateFlow(MenuDataState.Default)
    val menuDataState = _menuDataState.asStateFlow()

    fun loadAllProducts() {
        _menuDataState.value = MenuDataState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            val result = repository.loadAllProducts()
            withContext(Dispatchers.Main) {
                if(result.code == ResponseCode.RESPONSE_SUCCESSFUL && result.body != null) {
                    _menuDataState.value = MenuDataState.Loaded(
                        result.body!!.map { dto ->
                            MenuItemModel(
                                id = dto.id,
                                amount = dto.amount,
                                title = dto.title,
                                description = dto.description,
                                type = dto.type
                            )
                        }
                    )
                } else {
                    _menuDataState.value = MenuDataState.Error
                }
            }
        }
    }
}