package com.example.letspaanidriver.apis.classes


import com.google.gson.annotations.SerializedName

data class UserOrderDetailsRequest(
    @SerializedName("order_id")
    val orderId: String ,
    @SerializedName("user_id")
    val user_id: String
)