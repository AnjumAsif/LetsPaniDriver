package com.example.letspaanidriver.apis.classes


import com.google.gson.annotations.SerializedName

data class OrderAlertResponse(
    @SerializedName("message")
    val message: String,
    @SerializedName("avg_rating")
    val avg_rating: String,
    @SerializedName("orders")
    val orders: List<Order>,
    @SerializedName("status")
    val status: Boolean,

    @SerializedName("user")
    val userphoto: userphoto

) {
    data class Order(
        @SerializedName("amount")
        val amount: String,
        @SerializedName("coupon")
        val coupon: String,
        @SerializedName("created_at")
        val createdAt: String,
        @SerializedName("delivery_address")
        val deliveryAddress: String,
        @SerializedName("discount")
        val discount: String,
        @SerializedName("id")
        val id: Int,
        @SerializedName("payment_mode")
        val paymentMode: String,
        @SerializedName("status")
        val status: String,
        @SerializedName("tanker_type")
        val tankerType: String,
        @SerializedName("token")
        val token: String,
        @SerializedName("transaction_id")
        val transactionId: String,
        @SerializedName("trash")
        val trash: String,
        @SerializedName("updated_at")
        val updatedAt: String,
        @SerializedName("user_id")
        val userId: String,
        @SerializedName("user_name")
        val userName: String,
        @SerializedName("user_phone")
        val userPhone: String,
        @SerializedName("user_address")
        val ordersaddress: user_address
    )

    data class user_address(
        @SerializedName("house_no")
        val house_no: String,
        @SerializedName("address")
        val address: String)
}
data class userphoto(
    @SerializedName("photo")
    val photo: String
)

