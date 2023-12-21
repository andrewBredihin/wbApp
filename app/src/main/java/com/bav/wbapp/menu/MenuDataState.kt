package com.bav.wbapp.menu

import com.bav.wbapp.menu.model.MenuItemModel

sealed class MenuDataState {
    data object Default: MenuDataState()
    data object Loading: MenuDataState()
    data object Error: MenuDataState()
    data class Loaded(val data: List<MenuItemModel>): MenuDataState()
}
