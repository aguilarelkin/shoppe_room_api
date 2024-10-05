package com.androsh.shopee.ui.operation.offline

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.androsh.shopee.domain.models.ProductModel
import com.androsh.shopee.domain.repository.ProductRepositoryRoom
import com.androsh.shopee.ui.operation.OperationResult
import com.androsh.shopee.ui.operation.OperationUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class OperationOfflineViewModel @Inject constructor(
    private val productRepositoryRoom: ProductRepositoryRoom
) : ViewModel() {

    private val _productState = MutableStateFlow(ProductModel())
    val producState: StateFlow<ProductModel> = _productState

    private val _uiState = MutableStateFlow(OperationUiState())
    val uiState: StateFlow<OperationUiState> = _uiState

    fun initState() {
        _uiState.value = _uiState.value.copy(updateCreate = false)
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
                    productRepositoryRoom.getProduct(id)
                }
                _uiState.value =
                    _uiState.value.copy(product = result, isLoading = false)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = e.message, isLoading = false)
            }
        }
    }

    fun insertProduct(product: ProductModel) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            val result: OperationResult = withContext(Dispatchers.IO) {
                try {
                    productRepositoryRoom.addProduct(productModel = product)
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
                    isOperationSuccessResult = true,
                    isLoading = false
                )
            }
        }
    }

    fun updateProductDb(product: ProductModel) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            val result = withContext(Dispatchers.IO) {
                try {
                    productRepositoryRoom.updateProduct(product, product.id.toString())
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
                    isOperationSuccessResult = true,
                    isLoading = false
                )
            }
        }
    }

}