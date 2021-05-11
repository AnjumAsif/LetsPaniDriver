package com.example.letspani.apis.classes


import com.google.gson.annotations.SerializedName

data class SupportRequest(
    @SerializedName("message")
    val message: String,
    @SerializedName("subject")
    val subject: String,
    @SerializedName("user_id")
    val userId: String
)