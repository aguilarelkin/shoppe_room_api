package com.androsh.shopee.ui.operation.offline

import androidx.lifecycle.ViewModel
import com.androsh.shopee.domain.models.ProductModel
import com.androsh.shopee.domain.repository.ProductRepository
import com.androsh.shopee.domain.repository.ProductRepositoryRoom
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class OperationOfflineViewModel @Inject constructor(
    private val productRepository: ProductRepository,
    private val productRepositoryRoom: ProductRepositoryRoom
) : ViewModel(){

    private val _productState = MutableStateFlow(ProductModel())
    val producState :StateFlow<ProductModel> = _productState



}