package com.store.app.data.local.dao

import androidx.room.*
import com.store.app.data.local.entity.DealEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DealDao {
    @Query("SELECT * FROM deals WHERE isActive = 1")
    fun getAllActiveDeals(): Flow<List<DealEntity>>

    @Query("SELECT * FROM deals WHERE id = :dealId")
    fun getDealById(dealId: String): Flow<DealEntity?>

    @Query("SELECT * FROM deals WHERE endDate > :currentTime AND isActive = 1")
    fun getUpcomingDeals(currentTime: Long): Flow<List<DealEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDeals(deals: List<DealEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDeal(deal: DealEntity)

    @Query("DELETE FROM deals")
    suspend fun deleteAllDeals()

    @Query("UPDATE deals SET isActive = :isActive WHERE id = :dealId")
    suspend fun updateDealStatus(dealId: String, isActive: Boolean)
}
