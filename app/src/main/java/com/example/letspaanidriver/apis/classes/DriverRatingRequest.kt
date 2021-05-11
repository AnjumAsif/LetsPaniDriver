package com.example.letspaanidriver.apis.classes

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class DriverRatingRequest (
    @SerializedName("user_id")
    @Expose
    var user_id: String
)