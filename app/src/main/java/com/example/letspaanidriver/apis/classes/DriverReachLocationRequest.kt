package com.example.letspaanidriver.apis.classes


import com.google.gson.annotations.SerializedName

data class DriverReachLocationRequest(
    @SerializedName("order_id")
    val orderId: String
)