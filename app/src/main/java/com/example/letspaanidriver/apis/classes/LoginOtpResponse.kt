package com.example.letspani.apis.classes

import com.google.gson.annotations.SerializedName

data class LoginOtpResponse(
    @SerializedName("message")
    val message: String,
    @SerializedName("otp")
    val otp: String,
    @SerializedName("status")
    val status: Boolean
)