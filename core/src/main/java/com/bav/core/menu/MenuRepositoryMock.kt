package com.bav.core.menu

class MenuRepositoryMock : MenuRepository {
    override suspend fun loadAllProducts(): MenuResponseModel {
        return MenuResponseModel(
            200,
            "menu mock",
            listOf(
                MenuDto(
                    id = 1,
                    amount = 25,
                    title = "Title 1",
                    description = "Description 1",
                    type = "Type 1"
                ),
                MenuDto(
                    id = 2,
                    amount = 25,
                    title = "Title 2",
                    description = "Description 2",
                    type = "Type 2"
                ),
                MenuDto(
                    id = 3,
                    amount = 25,
                    title = "Title 3",
                    description = "Description 3",
                    type = "Type 3"
                ),
                MenuDto(
                    id = 4,
                    amount = 25,
                    title = "Title 4",
                    description = "Description 4",
                    type = "Type 4"
                ),
                MenuDto(
                    id = 5,
                    amount = 25,
                    title = "Title 5",
                    description = "Description 5",
                    type = "Type 5"
                ),MenuDto(
                    id = 6,
                    amount = 25,
                    title = "Title 6",
                    description = "Description 6",
                    type = "Type 6"
                )
            )
        )
    }
}