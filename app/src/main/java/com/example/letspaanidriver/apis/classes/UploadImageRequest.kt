package com.example.letspaanidriver.apis.classes

import com.google.gson.annotations.SerializedName

data class UploadImageRequest (

    @SerializedName("user_id")
    val user_id: Int,
    @SerializedName("type")
    val type: String,
    @SerializedName("document")
    val document: String
)