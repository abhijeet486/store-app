package com.store.app.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.store.app.data.local.dao.DealDao
import com.store.app.data.local.dao.ProductDao
import com.store.app.data.local.dao.UserDao
import com.store.app.data.local.entity.DealEntity
import com.store.app.data.local.entity.ProductEntity
import com.store.app.data.local.entity.UserEntity

@Database(
    entities = [UserEntity::class, ProductEntity::class, DealEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun productDao(): ProductDao
    abstract fun dealDao(): DealDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "store_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
