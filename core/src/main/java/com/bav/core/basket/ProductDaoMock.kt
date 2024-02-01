package com.bav.core.basket

class ProductDaoMock : ProductDao {

    private val list = listOf(
        ProductEntity(
            id = 0,
            productId = 0,
            amount = 25,
            title = "Product 1",
            type = "Type 1",
            price = 250f,
            amountInBasket = 5,
            available = true
        ),
        ProductEntity(
            id = 1,
            productId = 1,
            amount = 10,
            title = "Product 2",
            type = "Type 2",
            price = 500f,
            amountInBasket = 2,
            available = true
        )
    )

    override suspend fun getAll(): List<ProductEntity> {
        return list
    }

    override suspend fun getById(id: Int): ProductEntity {
        return ProductEntity(
            id = 2,
            productId = 2,
            amount = 10,
            title = "Product 2",
            type = "Type 2",
            price = 500f,
            amountInBasket = 2,
            available = true
        )
    }

    override suspend fun getByProductId(productId: Int): List<ProductEntity> {
        return list
    }

    override suspend fun getAmountInBasket(productId: Int): Int {
        return 1
    }

    override suspend fun update(product: ProductEntity) {

    }

    override suspend fun insert(product: ProductEntity) {

    }

    override suspend fun delete(product: ProductEntity) {

    }

    override suspend fun deleteAll() {

    }

    override suspend fun deleteByProductId(productId: Int) {

    }

    override suspend fun updateByProductId(productId: Int, amount: Int) {

    }
}