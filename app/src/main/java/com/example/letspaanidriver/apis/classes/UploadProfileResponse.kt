package com.example.letspaanidriver.apis.classes


import com.google.gson.annotations.SerializedName

data class UploadProfileResponse(
    @SerializedName("message")
    val message: String,
    @SerializedName("status")
    val status: Boolean
)