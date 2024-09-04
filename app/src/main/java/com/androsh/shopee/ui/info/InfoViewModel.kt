package com.androsh.shopee.ui.info

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
class InfoViewModel @Inject constructor(private val productoRepository: ProductRepository) :
    ViewModel() {
    private var _stateList = MutableStateFlow<List<ProductModel>>(emptyList())
    val stateList: StateFlow<List<ProductModel>> = _stateList

    fun getProducts() {
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                productoRepository.getProducts()
            }
            if (result.isNotEmpty()) {
                _stateList.value = result
            }
        }
    }
}