package com.store.app.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.store.app.StoreApplication
import com.store.app.data.local.entity.DealEntity
import com.store.app.data.local.entity.ProductEntity
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    private val productRepository = StoreApplication.getInstance().productRepository
    private val dealRepository = StoreApplication.getInstance().dealRepository

    private val _products = MutableLiveData<List<ProductEntity>>()
    val products: LiveData<List<ProductEntity>> = _products

    private val _deals = MutableLiveData<List<DealEntity>>()
    val deals: LiveData<List<DealEntity>> = _deals

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    init {
        loadData()
    }

    fun loadData() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            try {
                // First, try to fetch from API and cache
                productRepository.refreshProducts()
                dealRepository.refreshDeals()

                // Then observe from local database
                productRepository.getAllProducts().collectLatest { productList ->
                    _products.value = productList
                }
            } catch (e: Exception) {
                _error.value = e.message
            }
        }

        viewModelScope.launch {
            dealRepository.getAllActiveDeals().collectLatest { dealList ->
                _deals.value = dealList
            }
        }

        viewModelScope.launch {
            _isLoading.value = false
        }
    }

    fun refreshData() {
        loadData()
    }

    fun clearError() {
        _error.value = null
    }
}
