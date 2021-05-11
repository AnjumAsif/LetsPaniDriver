package com.example.letspaanidriver.apis.classes


import com.google.gson.annotations.SerializedName

data class DriverOrderHistoryRequest(
    @SerializedName("user_id")
    val user_id: String,

    @SerializedName("report_date")
    val report_date: String
)