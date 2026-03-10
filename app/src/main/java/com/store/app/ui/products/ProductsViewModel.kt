package com.store.app.ui.products

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.store.app.data.local.entity.ProductEntity
import com.store.app.data.repository.ProductRepository
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ProductsViewModel : ViewModel() {

    private val repository = ProductRepository()

    private val _products = MutableLiveData<List<ProductEntity>>()
    val products: LiveData<List<ProductEntity>> = _products

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    private var currentCategory = "All"

    init {
        loadProducts()
    }

    private fun loadProducts() {
        viewModelScope.launch {
            _isLoading.value = true
            repository.getAllProducts()
                .catch { e ->
                    _error.value = e.message
                    _isLoading.value = false
                }
                .collectLatest { productList ->
                    val filtered = if (currentCategory == "All") {
                        productList
                    } else {
                        productList.filter { it.category == currentCategory }
                    }
                    _products.value = filtered
                    _isLoading.value = false
                }
        }
    }

    fun refreshProducts() {
        loadProducts()
    }

    fun filterByCategory(category: String) {
        currentCategory = category
        loadProducts()
    }

    fun clearError() {
        _error.value = null
    }
}
