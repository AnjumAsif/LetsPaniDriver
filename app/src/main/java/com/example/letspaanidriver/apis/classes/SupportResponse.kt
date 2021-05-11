package com.example.letspani.apis.classes


import com.google.gson.annotations.SerializedName

data class SupportResponse(
    @SerializedName("message")
    val message: String,
    @SerializedName("status")
    val status: Boolean
)