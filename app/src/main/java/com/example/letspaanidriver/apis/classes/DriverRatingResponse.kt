package com.example.letspaanidriver.apis.classes

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class DriverRatingResponse(
    @SerializedName("status")
    @Expose
    var status: Boolean,
    @SerializedName("message")
    @Expose
    var message: String,
    @SerializedName("all_ratings")
    @Expose
    var allRatings: List<AllRating> = emptyList(),
    @SerializedName("rating")
    @Expose
    var rating: String
)

data class AllRating(
    @SerializedName("id")
    @Expose
    var id: String,
    @SerializedName("user_id")
    @Expose
    var userId: String,
    /*@SerializedName("order_id")
    @Expose
    var orderId: String,*/
    @SerializedName("rating")
    @Expose
    var rating: String,
    /*@SerializedName("rating_to")
    @Expose
    var rating_to: String,
    @SerializedName("message")
    @Expose
    var message: String,*/
    @SerializedName("status")
    @Expose
    var status: String,
    @SerializedName("created_at")
    @Expose
    var created_at: String
    /*@SerializedName("updated_at")
    @Expose
    var updated_at: String*/
)