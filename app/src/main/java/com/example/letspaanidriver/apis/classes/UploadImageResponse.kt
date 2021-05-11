package com.example.letspaanidriver.apis.classes

import com.google.gson.annotations.SerializedName

data class UploadImageResponse (

    @SerializedName("picture")
    val picture: String,
    @SerializedName("message")
    val message: String

)
