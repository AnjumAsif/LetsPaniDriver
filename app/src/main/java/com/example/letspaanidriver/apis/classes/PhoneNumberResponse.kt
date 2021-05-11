package com.example.letspani.apis.classes


import com.google.gson.annotations.SerializedName

data class PhoneNumberResponse(
    @SerializedName("message")
    val message: String,
    @SerializedName("status")
    val status: Boolean,
    @SerializedName("support_phone")
    val supportPhone: SupportPhone
) {
    data class SupportPhone(
        @SerializedName("created_at")
        val createdAt: String,
        @SerializedName("id")
        val id: Int,
        @SerializedName("_key")
        val key: String,
        @SerializedName("updated_at")
        val updatedAt: String,
        @SerializedName("_value")
        val value: String
    )
}