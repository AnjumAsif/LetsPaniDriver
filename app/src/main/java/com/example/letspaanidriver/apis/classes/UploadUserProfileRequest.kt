package com.example.letspaanidriver.apis.classes


import com.google.gson.annotations.SerializedName

data class UploadUserProfileRequest(
    @SerializedName("user_id")
    val user_id: String,
    @SerializedName("picture")
    val document: String
)