package com.androsh.shopee.domain.models

import com.androsh.shopee.data.database.entities.ProductDataRoom

data class ProductModel(
    val id: Int = 0,
    val title: String = "",
    val description: String = "",
    val price: Double = 0.0,
    val discountPercentage: Double = 0.0,
    val rating: Double = 0.0,
    val stock: Int = 0,
    val brand: String? = "",
    val category: String = "",
    val thumbnail: String = "",
    val images: List<String> = emptyList()
) {
    fun toDomainModel() = ProductDataRoom(
        id,
        title,
        description,
        price,
        discountPercentage,
        rating,
        stock,
        brand,
        category,
        thumbnail,
       images.joinToString(separator = ",")
    )
}
