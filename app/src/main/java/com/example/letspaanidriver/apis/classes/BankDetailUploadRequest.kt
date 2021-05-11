package com.example.letspaanidriver.apis.classes

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class BankDetailUploadRequest(
    @SerializedName("user_id")
    @Expose
    var userId: Int,
    @SerializedName("bank_name")
    @Expose
    var bankName: String,
    @SerializedName("account_number")
    @Expose
    var accountNumber: String,
    @SerializedName("ifsc_code")
    @Expose
    var ifscCode: String
)