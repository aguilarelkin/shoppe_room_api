package com.androsh.shopee.domain.models

import com.androsh.shopee.data.database.entities.CategoryDataRoom

data class Category(
    val name: String,
    val url: String
) {
    fun toDomainModel() = CategoryDataRoom(name = name, url = url)
    fun toDomainModelUrl() = CategoryDataRoom(name = name, url = url)
}
