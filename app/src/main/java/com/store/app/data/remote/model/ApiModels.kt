package com.store.app.data.remote.model

import com.google.gson.annotations.SerializedName

data class UserModel(
    @SerializedName("id")
    val id: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("displayName")
    val displayName: String,
    @SerializedName("photoUrl")
    val photoUrl: String?
)

data class ProductModel(
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("price")
    val price: Double,
    @SerializedName("imageUrl")
    val imageUrl: String,
    @SerializedName("category")
    val category: String,
    @SerializedName("inStock")
    val inStock: Boolean,
    @SerializedName("rating")
    val rating: Float,
    @SerializedName("reviewCount")
    val reviewCount: Int
)

data class DealModel(
    @SerializedName("id")
    val id: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("originalPrice")
    val originalPrice: Double,
    @SerializedName("discountedPrice")
    val discountedPrice: Double,
    @SerializedName("discountPercentage")
    val discountPercentage: Int,
    @SerializedName("imageUrl")
    val imageUrl: String,
    @SerializedName("startDate")
    val startDate: Long,
    @SerializedName("endDate")
    val endDate: Long,
    @SerializedName("isActive")
    val isActive: Boolean,
    @SerializedName("productId")
    val productId: String?
)

data class ApiResponse<T>(
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("data")
    val data: T?,
    @SerializedName("message")
    val message: String?
)
