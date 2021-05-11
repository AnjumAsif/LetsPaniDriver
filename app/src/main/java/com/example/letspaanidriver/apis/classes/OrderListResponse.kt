package com.example.letspaanidriver.apis.classes


import com.google.gson.annotations.SerializedName

data class OrderListResponse(
    @SerializedName("message")
    val message: String,
    @SerializedName("orders")
    val orders: List<Order>,
    @SerializedName("status")
    val status: Boolean
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
        val discount: Any?,
        @SerializedName("id")
        val id: Int,
        @SerializedName("payment_mode")
        val paymentMode: String,
        @SerializedName("status")
        val status: String,
        @SerializedName("tanker_type")
        val tankerType: String,
        @SerializedName("token")
        val token: Any?,
        @SerializedName("transaction_id")
        val transactionId: String,
        @SerializedName("trash")
        val trash: String,
        @SerializedName("updated_at")
        val updatedAt: String,
        @SerializedName("user_address")
        val userAddress: UserAddress,
        @SerializedName("user_id")
        val userId: String,
        @SerializedName("user_name")
        val userName: String,
        @SerializedName("user_phone")
        val userPhone: String
    ) {
        data class UserAddress(
            @SerializedName("address")
            val address: String,
            @SerializedName("created_at")
            val createdAt: String,
            @SerializedName("created_by")
            val createdBy: Any?,
            @SerializedName("id")
            val id: Int,
            @SerializedName("lang")
            val lang: String,
            @SerializedName("lat")
            val lat: String,
            @SerializedName("locality")
            val locality: Any?,
            @SerializedName("status")
            val status: String,
            @SerializedName("trash")
            val trash: String,
            @SerializedName("type")
            val type: String,
            @SerializedName("updated_at")
            val updatedAt: String,
            @SerializedName("updated_by")
            val updatedBy: Any?,
            @SerializedName("user_id")
            val userId: String
        )
    }
}