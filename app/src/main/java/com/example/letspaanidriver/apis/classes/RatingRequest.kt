package com.example.letspani.apis.classes


import com.google.gson.annotations.SerializedName

data class RatingRequest(
    @SerializedName("order_id")
    val orderId: String,
    @SerializedName("rating")
    val rating: Int,
    @SerializedName("user_id")
    val userId: String
)