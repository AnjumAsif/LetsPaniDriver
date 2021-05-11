package com.example.letspaanidriver.apis.classes


import com.google.gson.annotations.SerializedName

data class OrderDetailsRequest(
    @SerializedName("order_id")
    val orderId: String
)