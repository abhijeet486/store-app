package com.store.app.ui.deals

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.store.app.StoreApplication
import com.store.app.data.local.entity.DealEntity
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class DealsViewModel : ViewModel() {

    private val dealRepository = StoreApplication.getInstance().dealRepository

    private val _deals = MutableLiveData<List<DealEntity>>()
    val deals: LiveData<List<DealEntity>> = _deals

    private val _selectedDeal = MutableLiveData<DealEntity?>()
    val selectedDeal: LiveData<DealEntity?> = _selectedDeal

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    init {
        loadDeals()
    }

    fun loadDeals() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                dealRepository.refreshDeals()
                dealRepository.getAllActiveDeals().collectLatest { dealList ->
                    _deals.value = dealList
                    _isLoading.value = false
                }
            } catch (e: Exception) {
                _error.value = e.message
                _isLoading.value = false
            }
        }
    }

    fun selectDeal(deal: DealEntity) {
        _selectedDeal.value = deal
    }

    fun clearSelectedDeal() {
        _selectedDeal.value = null
    }

    fun refreshDeals() {
        loadDeals()
    }

    fun clearError() {
        _error.value = null
    }
}
