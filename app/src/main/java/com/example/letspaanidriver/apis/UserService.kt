package com.example.letspani.apis

import com.example.letspaanidriver.apis.classes.*
import com.example.letspani.apis.classes.*
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface UserService {
    @POST("user/sendloginotp")
    fun getLoginOtpResponse(@Header("Content-Type") content_type: String, @Body request: LoginOtpRequest): Call<LoginOtpResponse>

    @POST("driver/register")
    fun getRegestrationResponse(@Header("Content-Type") content_type: String, @Body request: RegisterDriverRequest): Call<RegisterDriverResponse>

    @POST("user/verifyloginotp")
    fun getOTPResponse(@Header("Content-Type") content_type: String, @Body request: OTPRequest): Call<OTPResponse>

    @POST("updateTankerStatus")
    fun getTankerStatusResponse(@Header("Content-Type") content_type: String, @Body request: TankerStatusRequest): Call<TankerStatusResponse>

    @POST("driverlivelocations")
    fun getDriverLiveResponse(@Header("Content-Type") content_type: String, @Body request: LiveLocationRequest): Call<LiveLocationResponse>

    @POST("sendOrderAlertToDriver")
    fun getOrderAlertResponse(@Header("Content-Type") content_type: String, @Body request: OrderAlertRequest): Call<OrderAlertResponse>

    @POST("acceptOrderByDriver")
    fun getAcceptDriverResponse(@Header("Content-Type") content_type: String, @Body request: OrderAcceptDriverRequest): Call<OrderAcceptDriverResponse>

    @POST("orderDelivered")
    fun getOrderDriverResponse(@Header("Content-Type") content_type: String, @Body request: OrderDeliveredDriverRequest): Call<OrderDeliveredDriverResponse>

    @POST("orderdetails")
    fun getOrderDetail(@Header("Content-Type") content_type: String, @Body request: OrderDetailRequest): Call<OrderDetailResponse>

    @POST("getCancelOrderReasonsDrivers")
    fun getCancelOrderResponse(@Header("Content-Type") content_type: String): Call<CancelOrderResponse>

    @POST("cancelOrderByDriver")
    fun getCurrentCancelOrderResponse(@Header("Content-Type") content_type: String, @Body request: CurrentCancelOrderRequest): Call<CurrentCancelOrderResponse>

    @POST("driverAtLocation")
    fun getDriverReachLocationResponse(@Header("Content-Type") content_type: String, @Body request: DriverReachLocationRequest): Call<DriverReachLocationResponse>

    @POST("getDriverOrderHistory")
    fun getDriverOrderHistoryResponse(@Header("Content-Type") content_type: String, @Body request: DriverOrderHistoryRequest): Call<DriverOrderHistoryResponse>

    @POST("startOrderDelivery")
    fun getDriverStartWaterDeliveryResponse(@Header("Content-Type") content_type: String, @Body request: DriverStartWaterDeliveryRequest): Call<DriverStartWaterDeliveryResponse>

    @POST("supportEnquiry")
    fun getSupportResponse(@Header("Content-Type") content_type: String, @Body request: SupportRequest): Call<SupportResponse>

    @GET("getSupportPhone")
    fun getPhoneNumberResponse(@Header("Content-Type") content_type: String): Call<PhoneNumberResponse>

    @POST("getTankermanProfileData")
    fun getDriverProfile(@Header("Content-Type") content_type: String, @Body request: ProfileRequest): Call<ProfileResponse>

    @POST("uploadDriverDocument")
    fun getUploadResponse(@Header("Content-Type") content_type: String, @Body request: UploadImageRequest): Call<UploadImageResponse>


    @POST("uploadDriverDocument")
    fun getUploadDocumentResponse(@Header("Content-Type") content_type: String, @Body request: UploadDocumentRequest): Call<UploadDocumentResponse>

    @POST("driverOrders")
    fun getOrderListResponse(@Header("Content-Type") content_type: String, @Body request: OrderListRequest): Call<OrderListResponse>

    @POST("orderdetails")
    fun getOrderDetailsResponse(@Header("Content-Type") content_type: String, @Body request: OrderDetailsRequest): Call<OrderDetailsResponse>

    @POST("userOrderDetails")
    fun getuserOrderDetailsResponse(@Header("Content-Type") content_type: String, @Body request: UserOrderDetailsRequest): Call<UserOrderDetailsResponse>

    @POST("createUserRating")
    fun getRatingResponse(@Header("Content-Type") content_type: String, @Body request: RatingRequest): Call<RatingResponse>


    @POST("getDriverLocation")
    fun getOrderPrefranceResponse(@Header("Content-Type") content_type: String, @Body request: PreferedLocationRequest): Call<PreferedLocationResponse>


    @POST("getDriverEarningHistory")
    fun getDriverAllEarning(@Header("Content-Type") content_type: String, @Body request: DriverEarningRequest): Call<DriverEarningResponse>


    @POST("uploadTankerDocument")
    fun getVehicleUploadDocumentResponse(@Header("Content-Type") content_type: String, @Body request: UploadDocumentRequest): Call<UploadDocumentResponse>

    @POST("getDriverDocument")
    fun getDriverDocument(@Header("Content-Type") content_type: String, @Body request: DownloadImageRequest): Call<DownloadImageResponse>

    @POST("getUserRating")
    fun getUserRating(@Header("Content-Type") content_type: String, @Body request: DriverRatingRequest): Call<DriverRatingResponse>

    @POST("updateTankermanProfile")
    fun updateTankermanProfile(@Header("Content-Type") content_type: String, @Body request: BankDetailUploadRequest): Call<BankDetailUploadResponse>


    @POST("updateUserPhoto")
    fun getUploadUserProfileResponse(@Header("Content-Type") content_type: String, @Body request: UploadUserProfileRequest): Call<UploadProfileResponse>

}