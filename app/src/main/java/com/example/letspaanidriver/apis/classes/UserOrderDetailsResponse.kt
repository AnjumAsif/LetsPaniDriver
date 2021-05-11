package com.example.letspaanidriver.apis.classes


import com.google.gson.annotations.SerializedName

data class UserOrderDetailsResponse(
    @SerializedName("message")
    val message: String,
    @SerializedName("order_details")
    val orderDetailss: OrderDetailss,
    @SerializedName("status")
    val status: Boolean
) {
    data class OrderDetailss(
        @SerializedName("name")
        val name: String,
        @SerializedName("rating")
        val rating: String
    )
}