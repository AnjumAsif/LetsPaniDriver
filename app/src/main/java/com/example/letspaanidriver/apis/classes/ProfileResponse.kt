package com.example.letspaanidriver.apis.classes

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class ProfileData (
    @SerializedName("id")
    @Expose
    var id:Int,
    @SerializedName("name")
    @Expose
    var name:String,
    @SerializedName("email")
    @Expose
    var email:String,
    @SerializedName("contact_number")
    @Expose
    var contactNumber:String,
    @SerializedName("address")
    @Expose
    var address:String,
    @SerializedName("tanker_type")
    @Expose
    var tankerType:String,
    @SerializedName("photo")
    @Expose
    var photo:String,
    @SerializedName("status")
    @Expose
    var status:Int,
    @SerializedName("is_online")
    @Expose
    var isOnline:Int,
    @SerializedName("is_available")
    @Expose
    var isAvailable:Int,
    @SerializedName("trash")
    @Expose
    var trash:Int,
    @SerializedName("created_by")
    @Expose
    var createdBy:Any,
    @SerializedName("updated_by")
    @Expose
    var updatedBy:Int,
    @SerializedName("device_token")
    @Expose
    var deviceToken:String,
    @SerializedName("referral_code")
    @Expose
    var referralCode:Any,
    @SerializedName("redeem_points")
    @Expose
    var redeemPoints:Any,
    @SerializedName("referral_by")
    @Expose
    var referralBy:Any,
    @SerializedName("created_at")
    @Expose
    var createdAt:String,
    @SerializedName("updated_at")
    @Expose
    var updatedAt:String,
    @SerializedName("user_role")
    @Expose
    var userRole:UserRole,
    @SerializedName("tankerman_details")
    @Expose
    var tankermanDetails:TankermanDetails,
    @SerializedName("tankerman_preferred_locations")
    @Expose
    var tankermanPreferredLocations:List<TankermanPreferredLocation>,
    @SerializedName("vehicles")
    @Expose
    var vehicles:Vehicles,
    @SerializedName("wallet")
    @Expose
    val wallet: Wallet
)
class ProfileResponse (
    @SerializedName("status")
    @Expose
    var status:Boolean,
    @SerializedName("message")
    @Expose
    var message:String,
    @SerializedName("profile_data")
    @Expose
    var profileData:ProfileData
    )
class TankermanDetails (
    @SerializedName("id")
    @Expose
    var id:Int,
    @SerializedName("user_id")
    @Expose
    var userId:Int,
    @SerializedName("gender")
    @Expose
    var gender:String,
    @SerializedName("dob")
    @Expose
    var dob:String,
    @SerializedName("licence_id")
    @Expose
    var licenceId:String,
    @SerializedName("licence_expiry_date")
    @Expose
    var licenceExpiryDate:String,

    @SerializedName("city")
    @Expose
    var city:String,
    @SerializedName("state")
    @Expose
    var state:String,
    @SerializedName("bank_name")
    @Expose
    var bankName:String,
    @SerializedName("account_number")
    @Expose
    var accountNumber:String,
    @SerializedName("ifsc_code")
    @Expose
    var ifscCode:String,
    @SerializedName("aadhar_number")
    @Expose
    var aadharNumber:String,
    @SerializedName("created_at")
    @Expose
    var createdAt:String,
    @SerializedName("updated_at")
    @Expose
    var updatedAt:String
)
class TankermanPreferredLocation (
    @SerializedName("id")
    @Expose
    var id:Int,
    @SerializedName("user_id")
    @Expose
    var userId:Int,
    @SerializedName("location_id")
    @Expose
    var locationId:Int,
    @SerializedName("created_at")
    @Expose
    var createdAt:String,
    @SerializedName("updated_at")
    @Expose
    var updatedAt:String
)
class UserRole (
    @SerializedName("id")
    @Expose
    var id:Int,
    @SerializedName("role_id")
    @Expose
    var roleId:Int,
    @SerializedName("user_id")
    @Expose
    var userId:Int,
    @SerializedName("created_at")
    @Expose
    var createdAt:String,
    @SerializedName("updated_at")
    @Expose
    var updatedAt:String
)
data class Vehicles(
    @SerializedName("created_at")
    val createdAt: String, // 2019-07-16 09:43:27
    @SerializedName("created_by")
    val createdBy: Int, // 1
    @SerializedName("id")
    val id: Int, // 30
    @SerializedName("image")
    val image: String, // http://letspaani.com/public/uploads/1563281007_1005_Link_PayTM.jpg
    @SerializedName("status")
    val status: Int, // 1
    @SerializedName("trash")
    val trash: Int, // 0
    @SerializedName("updated_at")
    val updatedAt: String, // 2019-07-16 09:43:27
    @SerializedName("updated_by")
    val updatedBy: Any?, // null
    @SerializedName("user_id")
    val userId: Int, // 193
    @SerializedName("vehicle_capacity")
    val vehicleCapacity: String, // 500 Ltr
    @SerializedName("vehicle_color")
    val vehicleColor: Any?, // null
    @SerializedName("vehicle_description")
    val vehicleDescription: String, // dsfdsg
    @SerializedName("vehicle_model")
    val vehicleModel: String, // 2012
    @SerializedName("vehicle_no")
    val vehicleNo: String, // RJ14-1888
    @SerializedName("vehicle_type")
    val vehicleType: String, // medium
    @SerializedName("vehicle_year")
    val vehicleYear: Any? // null
)

data class Wallet(
    @SerializedName("cash_in_hand")
    val cashInHand: Double, // 0
    @SerializedName("created_at")
    val createdAt: String, // 2020-03-29 22:12:46
    @SerializedName("id")
    val id: Int, // 6
    @SerializedName("updated_at")
    val updatedAt: String, // 2020-03-29 22:12:46
    @SerializedName("user_id")
    val userId: Int, // 193
    @SerializedName("wallet_money")
    val walletMoney: Int // 0
)