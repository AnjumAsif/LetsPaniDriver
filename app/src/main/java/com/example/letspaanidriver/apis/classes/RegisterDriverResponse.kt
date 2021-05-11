package com.example.letspaanidriver.apis.classes


import com.google.gson.annotations.SerializedName

data class RegisterDriverResponse(

    @SerializedName("message")
    val message: String,
    @SerializedName("status")
    val status: Boolean,
    @SerializedName("user_id")
    val userId: Int,
    @SerializedName("user_name")
    val user_name: String

)

