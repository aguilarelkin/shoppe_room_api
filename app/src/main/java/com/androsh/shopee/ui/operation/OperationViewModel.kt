package com.androsh.shopee.ui.operation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.androsh.shopee.domain.models.ProductModel
import com.androsh.shopee.domain.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class OperationViewModel @Inject constructor(
    private val productRepository: ProductRepository
) : ViewModel() {
    private val _productState = MutableStateFlow(ProductModel())
    val producState: StateFlow<ProductModel> = _productState

    private val _uiState = MutableStateFlow(OperationUiState())
    val uiState: StateFlow<OperationUiState> = _uiState

    fun initState() {
        _uiState.value = _uiState.value.copy(create = false, update = false)
    }

    fun initUiState() {
        _uiState.value = OperationUiState()
    }

    init {
        _uiState.value = OperationUiState()
    }

    fun onChangedField(
        id: Int,
        title: String,
        description: String,
        price: Double,
        discountPercentage: Double,
        rating: Double,
        stock: Int,
        brand: String,
        category: String,
        thumbnail: String,
        images: List<String>
    ) {
        _uiState.value = _uiState.value.copy(
            product = _uiState.value.product.copy(
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
                images
            )
        )
    }

    fun searchProduct(id: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                val result: ProductModel = withContext(Dispatchers.IO) {
                    productRepository.getProduct(id)
                }
                _uiState.value =
                    _uiState.value.copy(product = result, isLoading = false)
                _productState.value = result
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = e.message, isLoading = false)
            }
        }
    }

    fun addProduct(product: ProductModel) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            val result: OperationResult = withContext(Dispatchers.IO) {
                try {
                    productRepository.addProduct(productModel = product)
                    OperationResult.Success
                } catch (e: Exception) {
                    OperationResult.Error(e.message ?: "Unknown error")
                }
            }
            _uiState.value = when (result) {
                is OperationResult.Error -> _uiState.value.copy(
                    error = result.message,
                    isLoading = false
                )

                is OperationResult.Success -> _uiState.value.copy(
                    create = true,
                    isOperationSuccessResult = true,
                    isLoading = false
                )
            }
        }
    }

    fun updateProduct(product: ProductModel) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            val result = withContext(Dispatchers.IO) {
                try {
                    productRepository.updateProduct(product, product.id.toString())
                    OperationResult.Success
                } catch (e: Exception) {
                    OperationResult.Error(e.message ?: "Unknown error")
                }
            }
            _uiState.value = when (result) {
                is OperationResult.Error -> _uiState.value.copy(
                    error = result.message,
                    isLoading = false
                )

                is OperationResult.Success -> _uiState.value.copy(
                    update = true,
                    isOperationSuccessResult = true,
                    isLoading = false
                )
            }
        }
    }
}