package com.example.letspaanidriver.apis.classes


import com.google.gson.annotations.SerializedName

data class CancelOrderResponse(
    @SerializedName("cancel_reasons")
    val cancelReasons: List<CancelReason>,
    @SerializedName("message")
    val message: String,
    @SerializedName("status")
    val status: Boolean
) {
    data class CancelReason(
        @SerializedName("created_at")
        val createdAt: String,
        @SerializedName("id")
        val id: Int,
        @SerializedName("name")
        val name: String,
        @SerializedName("status")
        val status: String,
        @SerializedName("updated_at")
        val updatedAt: String
    )
}