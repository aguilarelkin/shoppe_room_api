package com.androsh.shopee.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.androsh.shopee.data.database.entities.CategoryDataRoom
import com.androsh.shopee.data.database.entities.ProductDataRoom

@Dao
interface DaoProduct {
    @Query("SELECT * FROM products")
    suspend fun getAllProducts(): List<ProductDataRoom>

    @Query("SELECT * FROM products WHERE id = :id")
    suspend fun getProduct(id: Int): ProductDataRoom

    @Query("SELECT * FROM category")
    suspend fun getCategories(): List<CategoryDataRoom>

    @Insert(entity = CategoryDataRoom::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun addCategory(title: List<CategoryDataRoom>): List<Long>

    @Query("SELECT * FROM products WHERE LOWER(title) LIKE '%' || LOWER(:data) || '%'")
    suspend fun searchProduct(data: String): List<ProductDataRoom>

    @Insert(entity = ProductDataRoom::class, onConflict = OnConflictStrategy.ABORT)
    suspend fun addProduct(productModel: ProductDataRoom): Long

    @Insert(entity = ProductDataRoom::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun addProducts(productModel: List<ProductDataRoom>): List<Long>

    @Update(entity = ProductDataRoom::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateProduct(
        productModel: ProductDataRoom
    ): Int

    @Query("DELETE FROM products WHERE id = :id")
    suspend fun deleteProduct(id: Int): Int
}