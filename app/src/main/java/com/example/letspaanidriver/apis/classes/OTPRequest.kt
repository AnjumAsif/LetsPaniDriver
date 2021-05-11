package com.example.letspani.apis.classes

import com.google.gson.annotations.SerializedName

data class OTPRequest(
    @SerializedName("contact_number")
    val contactNumber: String,
    @SerializedName("otp")
    val otp: String
)