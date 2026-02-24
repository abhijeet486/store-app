package com.store.app.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.store.app.StoreApplication
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NetworkReceiver : BroadcastReceiver() {
    
    companion object {
        var isNetworkAvailable = false
    }

    override fun onReceive(context: Context, intent: Intent) {
        isNetworkAvailable = isNetworkConnected(context)
        
        if (isNetworkAvailable) {
            // Refresh data when network becomes available
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val app = StoreApplication.getInstance()
                    app.productRepository.refreshProducts()
                    app.dealRepository.refreshDeals()
                } catch (e: Exception) {
                    // Handle error silently
                }
            }
        }
    }

    private fun isNetworkConnected(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        
        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
                capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
    }
}
