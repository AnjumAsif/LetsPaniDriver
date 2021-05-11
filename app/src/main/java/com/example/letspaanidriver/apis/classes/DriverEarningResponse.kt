package com.example.letspaanidriver.apis.classes

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class DriverEarningResponse(
    @SerializedName("status")
    @Expose
    var status: Boolean,
    @SerializedName("message")
    @Expose
    var message: String,
    @SerializedName("0")
    @Expose
    var zeroval: Obje
)

class Obje(
    @SerializedName("orders")
    @Expose
    var order: List<orders>,
    @SerializedName("admin_comission")
    @Expose
    var adminComission: AdminComission,
    @SerializedName("delivered_completed")
    @Expose
    var deliveredCompleted: Int,
    @SerializedName("earning")
    @Expose
    var earning: Int,
    @SerializedName("rating_earning")
    @Expose
    var ratingEarning: Int
)

class orders(
    @SerializedName("id")
    @Expose
    var id: Int,
    @SerializedName("user_id")
    @Expose
    var userId: Int,
    @SerializedName("user_name")
    @Expose
    var userName: String,
    @SerializedName("user_phone")
    @Expose
    var userPhone: String,
    @SerializedName("tanker_type")
    @Expose
    var tankerType: String,
    @SerializedName("transaction_id")
    @Expose
    var transactionId: String,
    @SerializedName("amount")
    @Expose
    var amount: String,
    @SerializedName("payment_mode")
    @Expose
    var paymentMode: String,
    @SerializedName("coupon")
    @Expose
    var coupon: Any,
    @SerializedName("discount")
    @Expose
    var discount: Any,
    @SerializedName("token")
    @Expose
    var token: String,
    @SerializedName("delivery_address")
    @Expose
    var deliveryAddress: Int,
    @SerializedName("status")
    @Expose
    var status: Int,
    @SerializedName("cancel_reason")
    @Expose
    var cancelReason: Any,
    @SerializedName("cancel_by")
    @Expose
    var cancelBy: Any,
    @SerializedName("payment_status")
    @Expose
    var paymentStatus: Int,
    @SerializedName("trash")
    @Expose
    var trash: Int,
    @SerializedName("created_at")
    @Expose
    var created_at: String,
    @SerializedName("updated_at")
    @Expose
    var updatedAt: String
)


class AdminComission(
    @SerializedName("id")
    @Expose
    var id: Int,
    @SerializedName("_key")
    @Expose
    var key: String,
    @SerializedName("_value")
    @Expose
    var value: String,
    @SerializedName("created_at")
    @Expose
    var createdAt: String,
    @SerializedName("updated_at")
    @Expose
    var updatedAt: String
)
/*class DriverEarningResponse(

    @SerializedName("status")
    @Expose
    var status: Boolean,

    @SerializedName("message")
    @Expose
    var message: String,

    @SerializedName("0")
    @Expose
    var zeroval: Obje


)

data class Obje(
    @SerializedName("orders")
    @Expose
    val order: List<orders>
)

data class orders(
    @SerializedName("id")
    val id: String,
    @SerializedName("amount")
    val amount: String,
    @SerializedName("created_at")
    val created_at: String
)*/





