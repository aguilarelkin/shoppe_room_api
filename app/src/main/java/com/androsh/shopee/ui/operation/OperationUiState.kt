package com.androsh.shopee.ui.operation

import com.androsh.shopee.domain.models.ProductModel

data class OperationUiState(
    val create: Boolean = false,
    val update: Boolean = false,
    val product: ProductModel = ProductModel(),
    val isOperationSuccessResult: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null
)
