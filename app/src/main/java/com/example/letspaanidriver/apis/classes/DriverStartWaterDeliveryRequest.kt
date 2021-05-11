package com.example.letspaanidriver.apis.classes


import com.google.gson.annotations.SerializedName

data class DriverStartWaterDeliveryRequest(
    @SerializedName("order_id")
    val order_id: String)

