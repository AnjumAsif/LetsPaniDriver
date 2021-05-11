package com.example.letspani.apis.classes


import com.google.gson.annotations.SerializedName

data class RatingResponse(
    @SerializedName("message")
    val message: String,
    @SerializedName("status")
    val status: Boolean
)