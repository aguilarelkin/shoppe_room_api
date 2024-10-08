package com.androsh.shopee.ui.info

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.androsh.shopee.domain.models.Category
import com.androsh.shopee.domain.models.ProductModel
import com.androsh.shopee.domain.repository.ProductRepository
import com.androsh.shopee.domain.repository.ProductRepositoryRoom
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class InfoViewModel @Inject constructor(
    private val productoRepository: ProductRepository,
    private val productRepositoryRoom: ProductRepositoryRoom
) :
    ViewModel() {

    private val _uiState = MutableStateFlow(InfoUiState())
    val uiState: StateFlow<InfoUiState> = _uiState

    init {
        getProducts()
        getCategories()
    }

    fun onChangedQuery(query: String) {
        _uiState.value = _uiState.value.copy(query = query)
    }

    fun onChangedUiState() {
        _uiState.value = InfoUiState()
    }

    private fun getProducts() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            val result = withContext(Dispatchers.IO) {
                productoRepository.getProducts()
            }
            if (result.isNotEmpty()) {
                productRepositoryRoom.addProducts(result)
            }
            _uiState.value = if (result.isNotEmpty()) {
                _uiState.value.copy(products = result, isLoading = false)

            } else {
                _uiState.value.copy(error = "Error", isLoading = false)
            }
        }
    }

    /**
     * Get List categories
     */
    private fun getCategories() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            val result: List<Category> = withContext(Dispatchers.IO) {
                productoRepository.getCategories()
            }
            if (result.isNotEmpty()) {
                productRepositoryRoom.addCategory(result)
            }
            _uiState.value = if (result.isNotEmpty()) {
                _uiState.value.copy(categories = result, isLoading = false)

            } else {
                _uiState.value.copy(error = "Error", isLoading = false)
            }

        }
    }

    /**
     * Search product
     */
    fun searchProduct(data: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            val result: List<ProductModel> = withContext(Dispatchers.IO) {
                productoRepository.searchProduct(data)
            }
            _uiState.value = if (result.isNotEmpty()) {
                _uiState.value.copy(products = result, isLoading = false)

            } else {
                _uiState.value.copy(error = "Error", isLoading = false)
            }
        }
    }

    /**
     * Delete product id
     */
    fun deleteProductId(id: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            val result: ProductModel = withContext(Dispatchers.IO) {
                productoRepository.deleteProduct(id)
            }
            _uiState.value = if (result.title.isNotEmpty()) {
                _uiState.value.copy(
                    isProductDeleted = true,
                    products = emptyList(),
                    isLoading = false
                )
            } else {
                _uiState.value.copy(error = "Error", isLoading = false)
            }
        }
    }

    /**
     * Filter products of the list of productmodel
     */
    fun filterProducts(filter: String) {
        _uiState.value = _uiState.value.copy(isLoading = true, isFiltering = true)

        val sortedProducts = when (filter) {
            "Precio" -> uiState.value.products.sortedBy { it.price }
            "Descuento" -> uiState.value.products.sortedBy { it.discountPercentage }
            "Categoria" -> uiState.value.products.sortedBy { it.category }
            "Stock" -> uiState.value.products.sortedBy { it.stock }
            "Marca" -> uiState.value.products.sortedBy { it.brand }
            "Rating" -> uiState.value.products.sortedBy { it.rating }
            else -> {
                uiState.value.products.sortedByDescending { it.rating }
            }
        }
        _uiState.value =
            uiState.value.copy(products = sortedProducts, isFiltering = false, isLoading = false)
    }

}