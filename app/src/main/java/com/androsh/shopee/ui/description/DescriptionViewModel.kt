package com.androsh.shopee.ui.description

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
class DescriptionViewModel @Inject constructor(
    private val productRepository: ProductRepository,
    private val productRepositoryRoom: ProductRepositoryRoom
) : ViewModel() {

    private var _stateProduct = MutableStateFlow<ProductModel>(ProductModel())
    val stateProduct: StateFlow<ProductModel> = _stateProduct
    private var _stateLoading = MutableStateFlow<Boolean>(false)
    val stateLoading: StateFlow<Boolean> = _stateLoading

    fun initData() {
        _stateProduct.value = ProductModel()
        _stateLoading.value = false
    }

    /**
     * Get product for id
     */
    fun getProductId(id: String) {
        _stateLoading.value = true
        viewModelScope.launch {
            try {
                val result: ProductModel = withContext(Dispatchers.IO) {
                    productRepository.getProduct(id)
                }
                _stateProduct.value = result
                _stateLoading.value = false
            } catch (e: Exception) {
                Log.e("getProductId", "Error al obtener producto: ${e.message}")
            }
        }
    }

    /**
     * Get product for id room
     */
    fun getProductRoomId(id: String) {
        _stateLoading.value = true
        viewModelScope.launch {
            val result: ProductModel = withContext(Dispatchers.IO) {
                productRepositoryRoom.getProduct(id)
            }
            _stateLoading.value = false
            _stateProduct.value = result
        }
    }

}