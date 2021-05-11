package com.example.letspaanidriver.apis.classes


import com.google.gson.annotations.SerializedName

data class OrderDetailRequest(
    @SerializedName("order_id")
    val order_id: String
)