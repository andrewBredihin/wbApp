package com.bav.wbapp.menu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bav.core.api.ResponseCode
import com.bav.core.menu.MenuRepository
import com.bav.wbapp.menu.model.MenuItemModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MenuScreenViewModel(private val repository: MenuRepository) : ViewModel() {

    companion object {
        const val DEBOUNCE = 500L
    }

    private var _searchFlow = MutableStateFlow("")
    private var queryTextChangedJob: Job? = null

    private var _loadedList: List<MenuItemModel> = emptyList()

    private var _menuDataState: MutableStateFlow<MenuDataState> = MutableStateFlow(MenuDataState.Default)
    val menuDataState: StateFlow<MenuDataState> = _menuDataState.asStateFlow()

    @OptIn(FlowPreview::class)
    suspend fun search() {
        _searchFlow
            .debounce(DEBOUNCE)
            .collect { value ->
                _menuDataState.value = if (value.isEmpty()) {
                    MenuDataState.Loaded(_loadedList)
                } else {
                    MenuDataState.Loaded(
                        _loadedList.filter { item ->
                            item.title.lowercase().contains(value.lowercase()) || item.type.lowercase().contains(value.lowercase())
                        }
                    )
                }
            }
    }

    /**
     * Функция поиска по значению из SearchView.
     * Поиск осуществляется с задержкой в DEBOUNCE.
     * Если поступает новое значение query, то viewModelScope.launch отменяется и запускается новая,
     * не лучшее решение, поэтому буду искать решение, как сделать лучше
     */
    fun updateSearchFlow(query: String) {
        _searchFlow.value = query
        queryTextChangedJob?.cancel()
        queryTextChangedJob = viewModelScope.launch {
            search()
        }
    }

    fun loadAllProducts() {
        _menuDataState.value = MenuDataState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            val result = repository.loadAllProducts()
            withContext(Dispatchers.Main) {
                if (result.code == ResponseCode.RESPONSE_SUCCESSFUL && result.body != null) {
                    _loadedList = result.body!!.map { dto ->
                        MenuItemModel(
                            id = dto.id,
                            amount = dto.amount,
                            title = dto.title,
                            description = dto.description,
                            type = dto.type
                        )
                    }
                    _menuDataState.value = MenuDataState.Loaded(_loadedList)
                } else {
                    _menuDataState.value = MenuDataState.Error
                }
            }
        }
    }
}