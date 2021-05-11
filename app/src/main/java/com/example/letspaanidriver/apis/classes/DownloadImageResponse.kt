package com.example.letspaanidriver.apis.classes

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class DownloadImageResponse(
    @SerializedName("status")
    @Expose
    var status: Boolean,
    @SerializedName("message")
    @Expose
    var message: String,
    @SerializedName("document")
    @Expose
    var document: Document
)

class Document(
    @SerializedName("id")
    @Expose
    var id: Int,
    @SerializedName("user_id")
    @Expose
    var userId: Int,
    @SerializedName("document_path")
    @Expose
    var documentPath: String,
    @SerializedName("type")
    @Expose
    var type: String,
    @SerializedName("created_at")
    @Expose
    var createdAt: String,
    @SerializedName("updated_at")
    @Expose
    var updatedAt: String
)