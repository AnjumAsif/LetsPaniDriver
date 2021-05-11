package com.example.letspaanidriver.apis.classes


import com.google.gson.annotations.SerializedName

data class UploadDocumentRequest(
    @SerializedName("user_id")
    val user_id: String,
    @SerializedName("type")
    val type: String,
    @SerializedName("document")
    val document: String
)