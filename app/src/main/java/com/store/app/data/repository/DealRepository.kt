package com.store.app.data.repository

import com.store.app.data.local.dao.DealDao
import com.store.app.data.local.entity.DealEntity
import com.store.app.data.remote.api.MockApiService
import com.store.app.data.remote.model.DealModel
import kotlinx.coroutines.flow.Flow

class DealRepository(
    private val dealDao: DealDao,
    private val mockApiService: MockApiService
) {

    fun getAllActiveDeals(): Flow<List<DealEntity>> {
        return dealDao.getAllActiveDeals()
    }

    fun getDealById(dealId: String): Flow<DealEntity?> {
        return dealDao.getDealById(dealId)
    }

    fun getUpcomingDeals(): Flow<List<DealEntity>> {
        return dealDao.getUpcomingDeals(System.currentTimeMillis())
    }

    suspend fun refreshDeals(): Result<Unit> {
        return try {
            val result = mockApiService.getDeals()
            if (result.isSuccess) {
                val deals = result.getOrNull()?.map { it.toEntity() } ?: emptyList()
                dealDao.insertDeals(deals)
                Result.success(Unit)
            } else {
                Result.failure(Exception("Failed to fetch deals"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun fetchDealsFromApi(): List<DealEntity> {
        val result = mockApiService.getDeals()
        return if (result.isSuccess) {
            result.getOrNull()?.map { it.toEntity() } ?: emptyList()
        } else {
            emptyList()
        }
    }

    suspend fun markDealAsInactive(dealId: String) {
        dealDao.updateDealStatus(dealId, false)
    }

    private fun DealModel.toEntity(): DealEntity {
        return DealEntity(
            id = id,
            title = title,
            description = description,
            originalPrice = originalPrice,
            discountedPrice = discountedPrice,
            discountPercentage = discountPercentage,
            imageUrl = imageUrl,
            startDate = startDate,
            endDate = endDate,
            isActive = isActive,
            productId = productId
        )
    }
}
