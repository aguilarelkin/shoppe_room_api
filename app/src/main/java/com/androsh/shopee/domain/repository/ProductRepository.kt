package com.androsh.shopee.domain.repository

import com.androsh.shopee.domain.models.Category
import com.androsh.shopee.domain.models.ProductModel

interface ProductRepository {
    suspend fun getProducts(): List<ProductModel>
    suspend fun getProduct(id: String): ProductModel
    suspend fun getCategories(): List<Category>
    suspend fun searchProduct(data: String): List<ProductModel>
    suspend fun addProduct(productModel: ProductModel): ProductModel
    suspend fun updateProduct(productModel: ProductModel, id: String): ProductModel
    suspend fun deleteProduct(id: String): ProductModel
}