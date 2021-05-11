package com.example.letspaanidriver.apis.classes

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class ProfileRequest (
    @SerializedName("user_id")
    @Expose
    var userId: String

)