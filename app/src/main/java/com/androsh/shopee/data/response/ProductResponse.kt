package com.androsh.shopee.data.response

import com.androsh.shopee.domain.models.Product
import com.google.gson.annotations.SerializedName

data class ProductResponse(
    @SerializedName("products") val products: List<ProductDataResponse>
) {
    fun toDomain() = Product(products.map { it.toDomain() })
}
