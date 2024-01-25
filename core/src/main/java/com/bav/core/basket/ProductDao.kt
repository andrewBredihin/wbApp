package com.bav.core.basket

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface ProductDao {

    @Query("SELECT * FROM productentity")
    suspend fun getAll(): List<ProductEntity>

    @Query("SELECT * FROM productentity WHERE id=(:id)")
    suspend fun getById(id: Int): ProductEntity

    @Query("SELECT * FROM productentity WHERE product_id=(:productId)")
    suspend fun getByProductId(productId: Int): List<ProductEntity>

    @Query("SELECT amount_in_basket FROM productentity WHERE product_id=(:productId)")
    suspend fun getAmountInBasket(productId: Int): Int

    @Update
    suspend fun update(product: ProductEntity)

    @Insert
    suspend fun insert(product: ProductEntity)

    @Delete
    suspend fun delete(product: ProductEntity)

    @Query("DELETE FROM productentity")
    suspend fun deleteAll()

    @Query("DELETE FROM productentity WHERE product_id=(:productId)")
    suspend fun deleteByProductId(productId: Int)

    @Query("UPDATE productentity SET amount_in_basket=(:amount) WHERE product_id=(:productId)")
    suspend fun updateByProductId(productId: Int, amount: Int)
}