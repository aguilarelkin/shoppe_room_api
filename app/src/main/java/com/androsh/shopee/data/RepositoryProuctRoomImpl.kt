package com.androsh.shopee.data

import android.util.Log
import com.androsh.shopee.data.database.dao.DaoProduct
import com.androsh.shopee.domain.models.Category
import com.androsh.shopee.domain.models.ProductModel
import com.androsh.shopee.domain.repository.ProductRepositoryRoom
import javax.inject.Inject

class RepositoryProuctRoomImpl @Inject constructor(private val daoProduct: DaoProduct) :
    ProductRepositoryRoom {
    override suspend fun getProducts(): List<ProductModel> {
        runCatching { daoProduct.getAllProducts() }.onSuccess {
            return it.map { data -> data.toDomain() }
        }.onFailure { Log.i("DDBB", "Error: ${it.message}") }
        return emptyList()
    }

    override suspend fun getProduct(id: String): ProductModel {
        runCatching { daoProduct.getProduct(id.toInt()) }.onSuccess {
            return it.toDomain()
        }.onFailure { Log.i("DDBB", "Error: ${it.message}") }
        return ProductModel()
    }

    override suspend fun getCategories(): List<Category> {
        runCatching { daoProduct.getCategories() }.onSuccess {
            return it.map { data -> data.toDomain() }
        }.onFailure { Log.i("DDBB", "Error: ${it.message}") }
        return emptyList()
    }

    override suspend fun addCategory(category: List<Category>): Boolean {
        runCatching { daoProduct.addCategory(category.map { it.toDomainModel() }) }.onSuccess {
            return true
        }.onFailure { Log.i("DDBB", "Error: ${it.message}") }
        return false
    }

    override suspend fun searchProduct(data: String): List<ProductModel> {
        runCatching { daoProduct.searchProduct(data) }.onSuccess {
            return it.map { data -> data.toDomain() }
        }.onFailure { Log.i("DDBB", "Error: ${it.message}") }
        return emptyList()
    }

    override suspend fun addProduct(productModel: ProductModel): Boolean {
        runCatching { daoProduct.addProduct(productModel.toDomainModel()) }.onSuccess {
            return true
        }.onFailure { Log.i("DDBB", "Error: ${it.message}") }
        return false
    }

    override suspend fun addProducts(productModel: List<ProductModel>): Boolean {
        runCatching { daoProduct.addProducts(productModel.map { it.toDomainModel() }) }.onSuccess {
            return true
        }.onFailure { Log.i("DDBB", "Error: ${it.message}") }
        return false
    }

    override suspend fun updateProduct(productModel: ProductModel, id: String): Boolean {
        runCatching { daoProduct.updateProduct(productModel.toDomainModel()) }.onSuccess {
            return true
        }.onFailure { Log.i("DDBB", "Error: ${it.message}") }
        return false
    }

    override suspend fun deleteProduct(id: String): Boolean {
        runCatching { daoProduct.deleteProduct(id.toInt()) }.onSuccess {
            return true
        }.onFailure { Log.i("DDBB", "Error: ${it.message}") }
        return false
    }
}