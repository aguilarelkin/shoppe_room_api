package com.androsh.shopee.data.response

import com.androsh.shopee.domain.models.Category
import com.google.gson.annotations.SerializedName

data class CategoryDataResponse(
    @SerializedName("name") val name: String,
    @SerializedName("url") val url: String,
) {
    fun toDomain() = Category(
        name,
        url
    )
}
