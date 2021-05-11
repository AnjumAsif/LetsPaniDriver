package com.example.letspaanidriver.apis.classes


import com.google.gson.annotations.SerializedName

data class OrderAlertRequest(
    @SerializedName("user_id")
    val userId: String
)