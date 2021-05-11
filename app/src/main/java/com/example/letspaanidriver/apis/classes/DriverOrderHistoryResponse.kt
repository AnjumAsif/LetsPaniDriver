package com.example.letspaanidriver.apis.classes


import com.google.gson.annotations.SerializedName

data class DriverOrderHistoryResponse(
    @SerializedName("message")
    val message: String,
    @SerializedName("status")
    val status: Boolean,

    @SerializedName("0")
    val history: History
)

data class History(
    val order_request: String,
    val order_accepted: String,
    val cancel_bydriver: String,
    val cancel_byuser: String,
    val delivered_completed: String,
    val active_hours: String,
    val earning: String)


