package com.androsh.shopee.ui.operation

sealed class OperationResult {
    data object Success : OperationResult()
    data class Error(val message: String) : OperationResult()
}