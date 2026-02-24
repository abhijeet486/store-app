package com.store.app.data.repository

import com.store.app.data.local.dao.ProductDao
import com.store.app.data.local.entity.ProductEntity
import com.store.app.data.remote.api.MockApiService
import com.store.app.data.remote.model.ProductModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ProductRepository(
    private val productDao: ProductDao,
    private val mockApiService: MockApiService
) {

    fun getAllProducts(): Flow<List<ProductEntity>> {
        return productDao.getAllProducts()
    }

    fun getProductById(productId: String): Flow<ProductEntity?> {
        return productDao.getProductById(productId)
    }

    fun getProductsByCategory(category: String): Flow<List<ProductEntity>> {
        return productDao.getProductsByCategory(category)
    }

    fun getAllCategories(): Flow<List<String>> {
        return productDao.getAllCategories()
    }

    suspend fun refreshProducts(): Result<Unit> {
        return try {
            val result = mockApiService.getProducts()
            if (result.isSuccess) {
                val products = result.getOrNull()?.map { it.toEntity() } ?: emptyList()
                productDao.insertProducts(products)
                Result.success(Unit)
            } else {
                Result.failure(Exception("Failed to fetch products"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun fetchProductsFromApi(): List<ProductEntity> {
        val result = mockApiService.getProducts()
        return if (result.isSuccess) {
            result.getOrNull()?.map { it.toEntity() } ?: emptyList()
        } else {
            emptyList()
        }
    }

    private fun ProductModel.toEntity(): ProductEntity {
        return ProductEntity(
            id = id,
            name = name,
            description = description,
            price = price,
            imageUrl = imageUrl,
            category = category,
            inStock = inStock,
            rating = rating,
            reviewCount = reviewCount
        )
    }
}
