package com.example.letspaanidriver.apis.classes


import com.google.gson.annotations.SerializedName

data class OrderListRequest(
    @SerializedName("status")
    val status: String,
    @SerializedName("user_id")
    val userId: String
)