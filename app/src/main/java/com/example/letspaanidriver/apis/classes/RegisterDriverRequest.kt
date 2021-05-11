package com.example.letspaanidriver.apis.classes


import com.google.gson.annotations.SerializedName

data class RegisterDriverRequest(
    @SerializedName("address")
    val address: String,
    @SerializedName("tanker_type")
    val tankerType: String,
    @SerializedName("contact_number")
    val contactNumber: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("token")
    val token: String
)