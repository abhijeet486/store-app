package com.store.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "deals")
data class DealEntity(
    @PrimaryKey
    val id: String,
    val title: String,
    val description: String,
    val originalPrice: Double,
    val discountedPrice: Double,
    val discountPercentage: Int,
    val imageUrl: String,
    val startDate: Long,
    val endDate: Long,
    val isActive: Boolean = true,
    val productId: String? = null
)
