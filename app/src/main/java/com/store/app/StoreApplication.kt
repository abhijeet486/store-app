package com.store.app

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import com.store.app.data.local.AppDatabase
import com.store.app.data.remote.api.MockApiService
import com.store.app.data.repository.DealRepository
import com.store.app.data.repository.ProductRepository
import com.store.app.data.repository.UserRepository

class StoreApplication : Application() {

    lateinit var database: AppDatabase
        private set

    lateinit var userRepository: UserRepository
        private set

    lateinit var productRepository: ProductRepository
        private set

    lateinit var dealRepository: DealRepository
        private set

    lateinit var mockApiService: MockApiService
        private set

    override fun onCreate() {
        super.onCreate()
        instance = this
        initializeDatabase()
        initializeRepositories()
        createNotificationChannel()
    }

    private fun initializeDatabase() {
        database = AppDatabase.getInstance(this)
    }

    private fun initializeRepositories() {
        mockApiService = MockApiService()
        userRepository = UserRepository(database.userDao(), mockApiService)
        productRepository = ProductRepository(database.productDao(), mockApiService)
        dealRepository = DealRepository(database.dealDao(), mockApiService)
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                getString(R.string.notification_channel_name),
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = getString(R.string.notification_channel_description)
            }

            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }

    companion object {
        const val NOTIFICATION_CHANNEL_ID = "deal_notifications"

        @Volatile
        private var instance: StoreApplication? = null

        fun getInstance(): StoreApplication {
            return instance ?: throw IllegalStateException("Application not initialized")
        }
    }
}
