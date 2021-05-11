package com.example.letspaanidriver.apis.classes


import com.google.gson.annotations.SerializedName
import java.util.*

data class OrderDetailResponse(
    @SerializedName("status")
    val status: Boolean,

    @SerializedName("message")
    val message: String,

    @SerializedName("order_detailss")
    val order_detailss: Order_detailss
)

data class Order_detailss(
    val id: String,
    val user_id: String,
    val user_name: String,
    val user_phone: String,
    val tanker_type: String,
    val transaction_id: String,
    val amount: String,
    val payment_mode: String,
    val coupon: String,
    val discount: String,
    val token: String,
    val delivery_address: String,
    val status: String,
    val cancel_reason: String, @SerializedName("cancel_by") val cancel_by: Int,
    val payment_status: String,
    val trash: String,
    val created_at: String,
    val updated_at: String
    ,
    @SerializedName("user_address")
    val user_address: user_address,
    @SerializedName("user_rating")
    val user_rating: String
    , @SerializedName("driver_rating")
    val driver_rating: String
)

data class user_address(
    val ids: String,
    val user_id: String,
    val type: String,
    val lat: String,
    val lang: String,
    val house_no: String,
    val address: String,
    val locality: String,
    val statusadd: String,
    val trash: String,
    val id: String,
    val created_by: String,
    val updated_by: String,
    val created_at: String,
    val updated_at: String
)



