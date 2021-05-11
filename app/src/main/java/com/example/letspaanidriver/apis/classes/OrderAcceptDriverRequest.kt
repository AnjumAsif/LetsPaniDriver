package com.example.letspaanidriver.apis.classes


import com.google.gson.annotations.SerializedName

data class OrderAcceptDriverRequest(
    @SerializedName("order_id")
    val orderId: String,
    @SerializedName("status")
    val status: Int,
    @SerializedName("user_id")
    val userId: String
)