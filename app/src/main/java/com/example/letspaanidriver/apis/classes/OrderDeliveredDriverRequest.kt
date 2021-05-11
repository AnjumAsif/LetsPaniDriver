package com.example.letspaanidriver.apis.classes


import com.google.gson.annotations.SerializedName

data class OrderDeliveredDriverRequest(
    @SerializedName("order_id")
    val orderId: String,
    @SerializedName("status")
    val status: Int
)