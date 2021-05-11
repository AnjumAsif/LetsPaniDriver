package com.example.letspaanidriver.apis.classes


import com.google.gson.annotations.SerializedName

data class LiveLocationRequest(
    @SerializedName("lang")
    val lang: String,
    @SerializedName("lat")
    val lat: String,
    @SerializedName("locality")
    val locality: String,
    @SerializedName("user_id")
    val userId: String
)