package com.example.letspaanidriver.apis.classes

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.util.*


class PreferedLocationResponse (
    @SerializedName("message")
    val message: String,
    @SerializedName("locations")
    val locations: List<locations>,
    @SerializedName("status")
    val status: Boolean

)

data class locations(
    @SerializedName("location")
    val location: Objects
)

data class Objects(
    @SerializedName("location")
    val address: String

    )





