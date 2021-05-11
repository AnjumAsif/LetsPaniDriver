package com.example.letspaanidriver.apis.classes

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class BankDetailUploadResponse (
    @SerializedName("status")
    @Expose
    var status: Boolean,
    @SerializedName("message")
    @Expose
    var message: String
    )