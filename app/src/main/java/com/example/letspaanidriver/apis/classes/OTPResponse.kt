package com.example.letspani.apis.classes

import com.google.gson.annotations.SerializedName

data class OTPResponse(
    @SerializedName("message")
    val message: String,
    @SerializedName("status")
    val status: Boolean,
    @SerializedName("user")
    val user: User
) {
    data class User(
        @SerializedName("address")
        val address: String,
        @SerializedName("city")
        val city: Any?,
        @SerializedName("contact_number")
        val contactNumber: String,
        @SerializedName("created_at")
        val createdAt: String,
        @SerializedName("created_by")
        val createdBy: Any?,
        @SerializedName("email")
        val email: String,
        @SerializedName("id")
        val id: Int,
        @SerializedName("lang")
        val lang: String,
        @SerializedName("lat")
        val lat: String,
        @SerializedName("name")
        val name: String,
        @SerializedName("photo")
        val photo: String,
        @SerializedName("status")
        val status: String,
        @SerializedName("trash")
        val trash: String,
        @SerializedName("updated_at")
        val updatedAt: String,
        @SerializedName("updated_by")
        val updatedBy: Any?,
        @SerializedName("user_role")
        val userRole: UserRole,
        @SerializedName("vendor_id")
        val vendorId: Any?
    ) {
        data class UserRole(
            @SerializedName("created_at")
            val createdAt: String,
            @SerializedName("id")
            val id: Int,
            @SerializedName("role_id")
            val roleId: String,
            @SerializedName("updated_at")
            val updatedAt: String,
            @SerializedName("user_id")
            val userId: String
        )
    }
}