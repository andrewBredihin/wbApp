package com.bav.wbapp.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bav.wbapp.main.model.MainCategoryModel

class MainScreenViewModel : ViewModel() {

    /**
        Список категорий на главном экране
     */
    private val _mainDataList: MutableLiveData<List<MainCategoryModel>> = MutableLiveData()
    val mainDataList: LiveData<List<MainCategoryModel>> = _mainDataList

    init {
        _mainDataList.value = getMainScreenDataList()
    }

    // TODO() Вынести строки в ресурсы
    private fun getMainScreenDataList() =
        listOf(
            MainCategoryModel.MainCategoryBig(
                title = "Акции",
                imageUrl = "https://cdn.zeplin.io/594d116d8ab5d1e677728fa6/assets/464DCCE3-49FB-486D-872A-52EB5A1FAC1B.png",
                id = "1"
            ),
            MainCategoryModel.MainCategorySmall(
                title = "Меню",
                imageUrl = "https://cdn.zeplin.io/594d116d8ab5d1e677728fa6/assets/FF88BD57-DE84-48EE-B27D-3FB2BA3DA4AA.png",
                id = "2"
            ),
            MainCategoryModel.MainCategorySmall(
                title = "Рестораны",
                imageUrl = "https://cdn.zeplin.io/594d116d8ab5d1e677728fa6/assets/D868F7FF-7790-41AD-8B0A-1E1A8DCE16C8.png",
                id = "3"
            ),
            MainCategoryModel.MainCategorySmall(
                title = "Условия доставки",
                imageUrl = "https://cdn.zeplin.io/594d116d8ab5d1e677728fa6/assets/E0C21C79-8A8A-40E7-BEC6-544255F2E4D3.png",
                id = "4"
            ),
            MainCategoryModel.MainCategorySmall(
                title = "История заказов",
                imageUrl = "https://cdn.zeplin.io/594d116d8ab5d1e677728fa6/assets/2407CCD4-66FA-47D9-AE1E-B14596EE67D7.png",
                id = "5"
            ),
            MainCategoryModel.MainCategorySmall(
                title = "Оставить отзыв",
                imageUrl = "https://cdn.zeplin.io/594d116d8ab5d1e677728fa6/assets/11AD66EB-E5FA-4CFE-8E7D-BE6236C59B94.png",
                id = "6"
            ),
            MainCategoryModel.MainCategorySmall(
                title = "Личный кабинет",
                imageUrl = "https://cdn.zeplin.io/594d116d8ab5d1e677728fa6/assets/11AD66EB-E5FA-4CFE-8E7D-BE6236C59B94.png",
                id = "7"
            ),
            MainCategoryModel.MainCategorySmall(
                title = "О приложении",
                imageUrl = "https://cdn.zeplin.io/594d116d8ab5d1e677728fa6/assets/11AD66EB-E5FA-4CFE-8E7D-BE6236C59B94.png",
                id = "8"
            )
        )
}