package com.bav.core.basket

class ProductDaoMock : ProductDao {
    override suspend fun getAll(): List<ProductEntity> {
        return listOf(
            ProductEntity(
                id = 1,
                productId = 1,
                amount = 25,
                title = "Product 1",
                type = "Type 1",
                price = 250f,
                amountInBasket = 5,
                available = true
            ),
            ProductEntity(
                id = 2,
                productId = 2,
                amount = 10,
                title = "Product 2",
                type = "Type 2",
                price = 500f,
                amountInBasket = 2,
                available = true
            )
        )
    }

    override suspend fun getById(id: Int): ProductEntity {
        TODO("Not yet implemented")
    }

    override suspend fun getByProductId(productId: Int): List<ProductEntity> {
        TODO("Not yet implemented")
    }

    override suspend fun getAmountInBasket(productId: Int): Int {
        TODO("Not yet implemented")
    }

    override suspend fun update(product: ProductEntity) {
        TODO("Not yet implemented")
    }

    override suspend fun insert(product: ProductEntity) {
        TODO("Not yet implemented")
    }

    override suspend fun delete(product: ProductEntity) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAll() {
        TODO("Not yet implemented")
    }

    override suspend fun deleteByProductId(productId: Int) {
        TODO("Not yet implemented")
    }

    override suspend fun updateByProductId(productId: Int, amount: Int) {
        TODO("Not yet implemented")
    }
}