package com.example.letspani.apis.classes

import com.google.gson.annotations.SerializedName

data class LoginOtpRequest(
    @SerializedName("contact_number")
    val contactNumber: String,
    @SerializedName("user_type")
    val usertype: String,
    @SerializedName("token")
    val token: String
)