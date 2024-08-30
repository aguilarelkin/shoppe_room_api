package com.androsh.shopee.data

import android.util.Log
import com.androsh.shopee.data.network.ProductApiService
import com.androsh.shopee.domain.models.ProductModel
import com.androsh.shopee.domain.repository.ProductRepository
import javax.inject.Inject

class RepositoryProductImpl @Inject constructor(private val productApiService: ProductApiService) :
    ProductRepository {

    override suspend fun getProducts(): List<ProductModel> {
        runCatching { productApiService.getProducts() }.onSuccess {
            return it.products.sortedByDescending { order ->
                order.rating
            }.map { data ->
                data.toDomain()
            }
        }.onFailure { Log.i("Error Api", "Error: ${it.message}") }
        return emptyList()
    }

    override suspend fun getProduct(id: String): ProductModel {
        runCatching { productApiService.getProduct(id) }.onSuccess {
            return it.toDomain()
        }
            .onFailure { Log.i("Error Api", "Error: ${it.message}") }
        return ProductModel()
    }

    override suspend fun getCategories(): List<String> {
        runCatching { productApiService.getCategories() }.onSuccess { return it }
            .onFailure { Log.i("Error Api", "Error: ${it.message}") }
        return emptyList()
    }

    override suspend fun searchProduct(data: String): List<ProductModel> {
        runCatching { productApiService.searchProduct(data) }.onSuccess {
            return it.products.sortedByDescending { order ->
                order.rating
            }.map { data ->
                data.toDomain()
            }
        }.onFailure { Log.i("Error Api", "Error: ${it.message}") }
        return emptyList()
    }

    override suspend fun addProduct(productModel: ProductModel): List<String> {
        runCatching { productApiService.addProduct(productModel) }.onSuccess { return it }
            .onFailure { Log.i("Error Api", "Error: ${it.message}") }
        return emptyList()
    }

    override suspend fun updateProduct(productModel: ProductModel, id: String): List<String> {
        runCatching { productApiService.updateProduct(productModel, id) }.onSuccess { return it }
            .onFailure { Log.i("Error Api", "Error: ${it.message}") }
        return emptyList()
    }

    override suspend fun deleteProduct(id: String): ProductModel {
        runCatching { productApiService.deleteProduct(id) }.onSuccess {
            return it.toDomain()
        }
            .onFailure { Log.i("Error Api", "Error: ${it.message}") }
        return ProductModel()
    }
}