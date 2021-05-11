package com.example.letspaanidriver.apis.classes

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class PreferedLocationRequest (
    @SerializedName("user_id")
    @Expose
    var userId: String

)