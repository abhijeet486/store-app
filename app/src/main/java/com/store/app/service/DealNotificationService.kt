package com.store.app.service

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.store.app.R
import com.store.app.StoreApplication
import com.store.app.ui.home.MainActivity
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.first

class DealNotificationService : Service() {

    private val serviceScope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    
    companion object {
        const val NOTIFICATION_ID = 1001
        const val CHANNEL_ID = "deal_notifications"
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startForeground(NOTIFICATION_ID, createNotification("Checking for deals..."))
        
        serviceScope.launch {
            checkForDeals()
        }
        
        return START_STICKY
    }

    private suspend fun checkForDeals() {
        while (true) {
            try {
                val dealRepository = StoreApplication.getInstance().dealRepository
                val deals = dealRepository.getUpcomingDeals().first()
                
                if (deals.isNotEmpty()) {
                    val nextDeal = deals.first()
                    showDealNotification(nextDeal.title, nextDeal.description)
                }
                
                delay(30 * 60 * 1000) // Check every 30 minutes
            } catch (e: Exception) {
                delay(60 * 1000) // Retry after 1 minute on error
            }
        }
    }

    private fun showDealNotification(title: String, message: String) {
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        
        val pendingIntent = PendingIntent.getActivity(
            this, 
            0, 
            intent, 
            PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(getString(R.string.notification_title))
            .setContentText(title)
            .setStyle(NotificationCompat.BigTextStyle().bigText(message))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        val notificationManager = getSystemService(NotificationManager::class.java)
        notificationManager.notify(NOTIFICATION_ID + 1, notification)
    }

    private fun createNotification(content: String): Notification {
        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent, PendingIntent.FLAG_IMMUTABLE
        )

        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(getString(R.string.app_name))
            .setContentText(content)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentIntent(pendingIntent)
            .setOngoing(true)
            .build()
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
    }
}
