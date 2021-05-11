package com.example.letspaanidriver.apis.classes


import com.google.gson.annotations.SerializedName

data class TankerStatusResponse(
    @SerializedName("is_available")
    val isAvailable: String,
    @SerializedName("message")
    val message: String,
    @SerializedName("status")
    val status: Boolean
)