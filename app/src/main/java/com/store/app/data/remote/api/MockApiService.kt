package com.store.app.data.remote.api

import com.store.app.data.remote.model.DealModel
import com.store.app.data.remote.model.ProductModel
import com.store.app.data.remote.model.UserModel
import kotlinx.coroutines.delay

class MockApiService {

    suspend fun loginWithGoogle(idToken: String): Result<UserModel> {
        delay(1000)
        return Result.success(
            UserModel(
                id = "user_${System.currentTimeMillis()}",
                email = "user@example.com",
                displayName = "Demo User",
                photoUrl = "https://via.placeholder.com/150"
            )
        )
    }

    suspend fun getProducts(): Result<List<ProductModel>> {
        delay(500)
        return Result.success(getMockProducts())
    }

    suspend fun getDeals(): Result<List<DealModel>> {
        delay(500)
        return Result.success(getMockDeals())
    }

    suspend fun getProductById(productId: String): Result<ProductModel?> {
        delay(300)
        return Result.success(getMockProducts().find { it.id == productId })
    }

    suspend fun getDealById(dealId: String): Result<DealModel?> {
        delay(300)
        return Result.success(getMockDeals().find { it.id == dealId })
    }

    private fun getMockProducts(): List<ProductModel> {
        val currentTime = System.currentTimeMillis()
        return listOf(
            ProductModel(
                id = "prod_1",
                name = "Smartphone X",
                description = "Latest flagship smartphone with amazing features including high-resolution camera, long battery life, and powerful processor.",
                price = 999.99,
                imageUrl = "https://via.placeholder.com/300x200",
                category = "Electronics",
                inStock = true,
                rating = 4.5f,
                reviewCount = 1250
            ),
            ProductModel(
                id = "prod_2",
                name = "Wireless Earbuds",
                description = "Premium wireless earbuds with active noise cancellation and 24-hour battery life.",
                price = 149.99,
                imageUrl = "https://via.placeholder.com/300x200",
                category = "Electronics",
                inStock = true,
                rating = 4.3f,
                reviewCount = 850
            ),
            ProductModel(
                id = "prod_3",
                name = "Smart Watch",
                description = "Feature-rich smartwatch with health tracking, GPS, and water resistance.",
                price = 299.99,
                imageUrl = "https://via.placeholder.com/300x200",
                category = "Electronics",
                inStock = true,
                rating = 4.7f,
                reviewCount = 2100
            ),
            ProductModel(
                id = "prod_4",
                name = "Laptop Pro",
                description = "Powerful laptop for professionals with 16GB RAM and 512GB SSD.",
                price = 1299.99,
                imageUrl = "https://via.placeholder.com/300x200",
                category = "Electronics",
                inStock = true,
                rating = 4.6f,
                reviewCount = 780
            ),
            ProductModel(
                id = "prod_5",
                name = "Tablet Air",
                description = "Lightweight tablet with stunning display perfect for entertainment and work.",
                price = 599.99,
                imageUrl = "https://via.placeholder.com/300x200",
                category = "Electronics",
                inStock = false,
                rating = 4.4f,
                reviewCount = 560
            ),
            ProductModel(
                id = "prod_6",
                name = "Bluetooth Speaker",
                description = "Portable Bluetooth speaker with 360-degree sound and 12-hour battery.",
                price = 79.99,
                imageUrl = "https://via.placeholder.com/300x200",
                category = "Electronics",
                inStock = true,
                rating = 4.2f,
                reviewCount = 320
            ),
            ProductModel(
                id = "prod_7",
                name = "Gaming Console",
                description = "Next-gen gaming console with 4K graphics and exclusive games.",
                price = 499.99,
                imageUrl = "https://via.placeholder.com/300x200",
                category = "Electronics",
                inStock = true,
                rating = 4.9f,
                reviewCount = 5200
            ),
            ProductModel(
                id = "prod_8",
                name = "Camera DSLR",
                description = "Professional DSLR camera with 45MP sensor and 4K video recording.",
                price = 1899.99,
                imageUrl = "https://via.placeholder.com/300x200",
                category = "Electronics",
                inStock = true,
                rating = 4.8f,
                reviewCount = 340
            )
        )
    }

    private fun getMockDeals(): List<DealModel> {
        val currentTime = System.currentTimeMillis()
        val dayInMillis = 24 * 60 * 60 * 1000L
        
        return listOf(
            DealModel(
                id = "deal_1",
                title = "Summer Sale - 50% Off",
                description = "Get 50% off on all smartphones this summer. Limited time offer!",
                originalPrice = 999.99,
                discountedPrice = 499.99,
                discountPercentage = 50,
                imageUrl = "https://via.placeholder.com/400x200",
                startDate = currentTime - dayInMillis,
                endDate = currentTime + dayInMillis * 7,
                isActive = true,
                productId = "prod_1"
            ),
            DealModel(
                id = "deal_2",
                title = "Buy One Get One Free",
                description = "Buy any wireless earbuds and get another one free!",
                originalPrice = 149.99,
                discountedPrice = 149.99,
                discountPercentage = 50,
                imageUrl = "https://via.placeholder.com/400x200",
                startDate = currentTime - dayInMillis,
                endDate = currentTime + dayInMillis * 3,
                isActive = true,
                productId = "prod_2"
            ),
            DealModel(
                id = "deal_3",
                title = "Smart Watch Special",
                description = "Special discount on smart watches. Save $100 now!",
                originalPrice = 299.99,
                discountedPrice = 199.99,
                discountPercentage = 33,
                imageUrl = "https://via.placeholder.com/400x200",
                startDate = currentTime - dayInMillis * 2,
                endDate = currentTime + dayInMillis * 5,
                isActive = true,
                productId = "prod_3"
            ),
            DealModel(
                id = "deal_4",
                title = "Laptop Upgrade Deal",
                description = "Upgrade to Laptop Pro and save $200. Limited stock!",
                originalPrice = 1299.99,
                discountedPrice = 1099.99,
                discountPercentage = 15,
                imageUrl = "https://via.placeholder.com/400x200",
                startDate = currentTime,
                endDate = currentTime + dayInMillis * 10,
                isActive = true,
                productId = "prod_4"
            ),
            DealModel(
                id = "deal_5",
                title = "Gaming Bundle",
                description = "Get Gaming Console + 2 games at special bundle price!",
                originalPrice = 599.99,
                discountedPrice = 499.99,
                discountPercentage = 17,
                imageUrl = "https://via.placeholder.com/400x200",
                startDate = currentTime - dayInMillis * 3,
                endDate = currentTime + dayInMillis * 14,
                isActive = true,
                productId = "prod_7"
            ),
            DealModel(
                id = "deal_6",
                title = "Camera Flash Sale",
                description = "Professional camera at 40% off! Perfect for photography enthusiasts.",
                originalPrice = 1899.99,
                discountedPrice = 1139.99,
                discountPercentage = 40,
                imageUrl = "https://via.placeholder.com/400x200",
                startDate = currentTime,
                endDate = currentTime + dayInMillis * 2,
                isActive = true,
                productId = "prod_8"
            )
        )
    }
}
