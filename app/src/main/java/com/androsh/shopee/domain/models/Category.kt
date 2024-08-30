package com.androsh.shopee.domain.models

import com.androsh.shopee.data.database.entities.CategoryDataRoom

data class Category(
    val title: String
) {
    fun toDomainModel() = CategoryDataRoom(title = title)
}
