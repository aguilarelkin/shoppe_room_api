package com.androsh.shopee.data.network

import com.androsh.shopee.data.response.ProductDataResponse
import com.androsh.shopee.data.response.ProductResponse
import com.androsh.shopee.domain.models.ProductModel
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface ProductApiService {
    @GET("/products")
    suspend fun getProducts(): ProductResponse

    @GET("/products/{id}")
    suspend fun getProduct(@Path("id") id: String): ProductDataResponse

    @GET("/products/categories")
    suspend fun getCategories(): List<String>

    @GET("/products/search")
    suspend fun searchProduct(@Query("q") data: String): ProductResponse

    @POST("/products/add")
    suspend fun addProduct(@Body productModel: ProductModel): List<String>

    @PUT("/products/products/{id}")
    suspend fun updateProduct(
        @Body productModel: ProductModel,
        @Path("id") id: String
    ): List<String>

    @DELETE("/products/{id}")
    suspend fun deleteProduct(@Path("id") id: String): ProductDataResponse
}