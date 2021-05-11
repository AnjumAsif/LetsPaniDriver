package com.example.letspaanidriver.apis.classes


import com.google.gson.annotations.SerializedName

data class CurrentCancelOrderRequest(
    @SerializedName("cancel_by")
    val cancelBy: Int,
    @SerializedName("order_id")
    val orderId: String,
    @SerializedName("reason_id")
    val reasonId: Int
)