package com.store.app.ui.products

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.store.app.StoreApplication
import com.store.app.data.local.entity.ProductEntity
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ProductsViewModel : ViewModel() {

    private val productRepository = StoreApplication.getInstance().productRepository

    private val _products = MutableLiveData<List<ProductEntity>>()
    val products: LiveData<List<ProductEntity>> = _products

    private val _selectedProduct = MutableLiveData<ProductEntity?>()
    val selectedProduct: LiveData<ProductEntity?> = _selectedProduct

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    private val _categories = MutableLiveData<List<String>>()
    val categories: LiveData<List<String>> = _categories

    private var currentCategory: String? = null

    init {
        loadProducts()
        loadCategories()
    }

    fun loadProducts() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                productRepository.refreshProducts()
                productRepository.getAllProducts().collectLatest { productList ->
                    _products.value = productList
                    _isLoading.value = false
                }
            } catch (e: Exception) {
                _error.value = e.message
                _isLoading.value = false
            }
        }
    }

    fun loadCategories() {
        viewModelScope.launch {
            productRepository.getAllCategories().collectLatest { categoryList ->
                _categories.value = categoryList
            }
        }
    }

    fun filterByCategory(category: String?) {
        currentCategory = category
        viewModelScope.launch {
            _isLoading.value = true
            if (category == null) {
                productRepository.getAllProducts().collectLatest { productList ->
                    _products.value = productList
                    _isLoading.value = false
                }
            } else {
                productRepository.getProductsByCategory(category).collectLatest { productList ->
                    _products.value = productList
                    _isLoading.value = false
                }
            }
        }
    }

    fun selectProduct(product: ProductEntity) {
        _selectedProduct.value = product
    }

    fun clearSelectedProduct() {
        _selectedProduct.value = null
    }

    fun refreshProducts() {
        loadProducts()
    }

    fun clearError() {
        _error.value = null
    }
}
