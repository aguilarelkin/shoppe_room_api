package com.androsh.shopee.data

import android.util.Log
import com.androsh.shopee.data.network.ProductApiService
import com.androsh.shopee.domain.models.Category
import com.androsh.shopee.domain.models.ProductModel
import com.androsh.shopee.domain.repository.ProductRepository
import com.google.firebase.crashlytics.FirebaseCrashlytics
import javax.inject.Inject

class RepositoryProductImpl @Inject constructor(private val productApiService: ProductApiService) :
    ProductRepository {

    override suspend fun getProducts(): List<ProductModel> {
        runCatching {
            productApiService.getProducts()
        }.onSuccess {
            return it.products.sortedByDescending { order ->
                order.rating
            }.map { data ->
                //Exection
                data.toDomain()
            }
        }.onFailure {
            Log.i("Error Api", "Error: ${it}")
            FirebaseCrashlytics.getInstance().recordException(it)
        }/*        return runCatching {
            val product: List<ProductDataResponse> = productApiService.getProducts().products
            product.map {
                FirebaseCrashlytics.getInstance().log("asdfasdf ${it.toDomain()}")
                it.toDomain()
            }
        }*/
        return emptyList()
    }

    override suspend fun getProduct(id: String): ProductModel {
        runCatching { productApiService.getProduct(id) }.onSuccess {
            return it.toDomain()
        }.onFailure { Log.i("Error Api", "Error: ${it}") }
        return ProductModel()
    }

    override suspend fun getCategories(): List<Category> {
        runCatching { productApiService.getCategories() }.onSuccess {
            return it.map { data ->
                Log.d("CategoryDataResponse", "Converting: ${data.name}, ${data.url}")
                data.toDomain()
            }
        }.onFailure { Log.i("Error Api", "Error: ${it}") }
        return emptyList()
    }

    override suspend fun searchProduct(data: String): List<ProductModel> {
        runCatching { productApiService.searchProduct(data) }.onSuccess {
            return it.products.sortedByDescending { order ->
                order.rating
            }.map { data ->
                data.toDomain()
            }
        }.onFailure { Log.i("Error Api", "Error: ${it}") }
        return emptyList()
    }

    override suspend fun addProduct(productModel: ProductModel): ProductModel {
        runCatching { productApiService.addProduct(productModel) }.onSuccess {
            return it
        }.onFailure { Log.i("Error Api", "Error: ${it}") }
        return ProductModel()
    }

    override suspend fun updateProduct(productModel: ProductModel, id: String): ProductModel {
        runCatching { productApiService.updateProduct(productModel, id) }.onSuccess {
            Log.i("11aaaaaaaaaaaaaaaaaaaaaaaaaaaa", it.toString())
            return it
        }.onFailure { Log.i("Error Api", "Error: ${it}") }
        return ProductModel()
    }

    override suspend fun deleteProduct(id: String): ProductModel {
        runCatching { productApiService.deleteProduct(id) }.onSuccess {
            return it.toDomain()
        }.onFailure { Log.i("Error Api", "Error: ${it}") }
        return ProductModel()
    }
}