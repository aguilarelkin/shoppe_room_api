package com.androsh.shopee.domain.repository

import androidx.room.Dao
import com.androsh.shopee.domain.models.Category
import com.androsh.shopee.domain.models.ProductModel

@Dao
interface ProductRepositoryRoom {
    suspend fun getProducts(): List<ProductModel>
    suspend fun getProduct(id: String): ProductModel
    suspend fun getCategories(): List<Category>
    suspend fun addCategory(category: List<Category>): Boolean
    suspend fun searchProduct(data: String): List<ProductModel>
    suspend fun addProduct(productModel: ProductModel):Boolean
    suspend fun addProducts(productModel: List<ProductModel>):Boolean
    suspend fun updateProduct(productModel: ProductModel, id: String): Boolean
    suspend fun deleteProduct(id: String): Boolean
}