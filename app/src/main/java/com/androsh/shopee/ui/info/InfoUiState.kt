package com.androsh.shopee.ui.info

import com.androsh.shopee.domain.models.Category
import com.androsh.shopee.domain.models.ProductModel

data class InfoUiState(
    val products: List<ProductModel> = emptyList(),
    val categories: List<Category> = emptyList(),
    val query: String = "",
    val isFiltering: Boolean = false,
    val isProductDeleted: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null,
    val isProductCreated:Boolean = false
)
