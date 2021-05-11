package com.example.letspaanidriver.apis.classes

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class DownloadImageRequest(
    @SerializedName("user_id")
    @Expose
    var userId: String,
    @SerializedName("type")
    @Expose
    var type: String
)