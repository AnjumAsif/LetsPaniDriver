package com.example.letspaanidriver.activity

import android.Manifest
import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.letspaanidriver.R
import com.example.letspaanidriver.apis.classes.*
import com.example.letspani.apis.AppController
import com.example.letspani.apis.UserService
import com.example.letspani.utils.AlertUtils
import com.example.letspani.utils.UserShared
import kotlinx.android.synthetic.main.activity_reachlocation.*
import kotlinx.android.synthetic.main.dailog_location_reach_conformation.view.*
import kotlinx.android.synthetic.main.dialog_cancel_by_customer.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.DecimalFormat
import java.util.*


class ActivityStartWaterDelivery : AppCompatActivity() {
    private var orderDeliveryStatus = false
    private var ordersid = ""
    private var customeraddress = ""
    private var customername = ""
    private var customerpaymentmode = ""
    private var deliverytime = ""
    private var phonenumber = ""
    private var customer_id = ""
    private var delivery_address = ""
    var timeDistance: String = ""
    var customerLat: Double? = null
    var customerLong: Double? = null

    var handlerforCancel = Handler()
    var sharedPref: SharedPreferences? = null
    private var PRIVATE_MODE = 0

    private var reasonList: Array<String> = arrayOf()
    var res: List<CancelOrderResponse.CancelReason>? = null

    private var fineshbill = 0

    var DriverLat: Double? = null
    var DriverLong: Double? = null

    companion object {
        var timeDistance: Double? = null

    }
    var sharedPrefLanguage: SharedPreferences? = null
    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPrefLanguage = getSharedPreferences("language", PRIVATE_MODE)
        var langval = sharedPrefLanguage!!.getInt("lang", 0)
        if (langval == 1) {
            val locale = Locale("hi")
            Locale.setDefault(locale)
            val config = Configuration()
            config.locale = locale
            baseContext.resources.updateConfiguration(
                config,
                baseContext.resources.displayMetrics
            )
            setContentView(R.layout.activity_reachlocation)

        } else {
            setContentView(R.layout.activity_reachlocation)
        }
        /*  supportActionBar!!.setDisplayHomeAsUpEnabled(true)
          title = "Add"*/
        //actionbar
        val actionbar = supportActionBar
        //set actionbar title
        actionbar!!.title = getString(R.string.CURRENT_ORDER)
        actionbar.setBackgroundDrawable(ColorDrawable(Color.parseColor("#76D4FC")));
        //set back button
        actionbar.setDisplayHomeAsUpEnabled(true)
        actionbar.setDisplayHomeAsUpEnabled(true)
        sharedPref = getSharedPreferences("OrderDetail", PRIVATE_MODE)
        val orderActive = sharedPref!!.getString("orderid", "")
        rellayReachLoc.visibility = View.VISIBLE
        laybillDetails.visibility = View.GONE
        if (orderActive != "") {
            ordersid = sharedPref!!.getString("orderid", "")
            customeraddress = sharedPref!!.getString("customeraddress", "")
            customerLat = sharedPref!!.getString("customerlat", "").toDouble()
            customerLong = sharedPref!!.getString("customerlong", "").toDouble()
            customername = sharedPref!!.getString("customername", "")
            customerpaymentmode = sharedPref!!.getString("customerpaymentmode", "")
            deliverytime = sharedPref!!.getString("deliverytime", "")
            phonenumber = sharedPref!!.getString("phonenumber", "")
            customer_id = sharedPref!!.getString("customerid", "")
            delivery_address = sharedPref!!.getString("customerdelivery_address", "")
            DriverLat = sharedPref!!.getString("DriverLat", "").toDouble()
            DriverLong = sharedPref!!.getString("DriverLong", "").toDouble()
            customerLat = sharedPref!!.getString("customerlat", "").toDouble()
            customerLong = sharedPref!!.getString("customerlong", "").toDouble()
            txtorderid.setText(ordersid)
            txtname.setText(customername)
            txtcurrent_status_address.setText(customeraddress)
            txtpaymentmode.setText(customerpaymentmode)
            //txtdelivery_time.setText(deliverytime)
            var distance: Double = calculateDistance()
            Log.i("dfsfsd", "the distance is====$distance")
            var minute: Double = 4.0
            var time: Double
            time = minute * distance
            Log.i("dfdsfds", "the data is coming=====$time")
            timeDistance = (DecimalFormat("##.#")).format(time) + " min"


            txtdelivery_time.setText(timeDistance)


            if (ordersid != "") {
                doCancelCheck()
            }
        }

        phonecall.setOnClickListener {
            if (phonenumber != "") {
                val callIntent = Intent(Intent.ACTION_CALL)
                callIntent.data = Uri.parse("tel:${phonenumber}")

                if (ActivityCompat.checkSelfPermission(
                        applicationContext!!,
                        Manifest.permission.CALL_PHONE
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    return@setOnClickListener
                }
                startActivity(callIntent)

            }


        }



        cancelbutton.setOnClickListener {
            val reasons = reasonList

            var resionid = ""

            val alt_builder = AlertDialog.Builder(this@ActivityStartWaterDelivery)
            alt_builder.setTitle("Select a Cancelation Reason")
            alt_builder.setSingleChoiceItems(reasons, -1, DialogInterface.OnClickListener
            { dialogInterface, i ->

                resionid = res!!.get(i).id.toString()
                Toast.makeText(
                    this@ActivityStartWaterDelivery,
                    reasons!![i],
                    Toast.LENGTH_SHORT
                ).show()
                AlertUtils.dialog?.dismiss()

                Log.i("asdf", "cancelresion==" + res!!.get(i).id)
            })

            alt_builder.setPositiveButton(
                "Ok",
                DialogInterface.OnClickListener { dialogInterface, i ->

                    CancelTheCurrentOrder(
                        ordersid,
                        resionid,
                        UserShared.getUserID(applicationContext).toString()
                    )
                    dialogInterface.dismiss()

                })

            alt_builder.setNegativeButton(
                "Cancel",
                DialogInterface.OnClickListener { dialogInterface, i ->

                    dialogInterface.dismiss()
                })

            val alert = alt_builder.create()
            alert.show()
        }



        startdelivery.setOnClickListener {
            popupStartWaterDelivery();
        }

        btnfineshdelivery.setOnClickListener {
            popupFineshWaterDelivery()
        }

        btnBillOk.setOnClickListener {
            finish()
        }




        getCancellationReasons()

        orderDeliveryStatus = sharedPref!!.getBoolean("orderStartDeliverystatus", false)

        if (orderDeliveryStatus == true) {
            layDeliveryStary.visibility = View.GONE
            btnfineshdelivery.visibility = View.VISIBLE
            cancelbutton.visibility = View.GONE
        }
    }


    private fun popupStartWaterDelivery() {
        //Inflate the dialog with custom view
        val mDialogView =
            LayoutInflater.from(this).inflate(R.layout.dailog_startwatercelivery, null)
        //AlertDialogBuilder
        val mBuilder = AlertDialog.Builder(this)
            .setView(mDialogView)
        //show dialog
        val mAlertDialog = mBuilder.show()
        //login button click of custom layout
        mDialogView.YesReachLocation.setOnClickListener {

            mAlertDialog.dismiss()

            layDeliveryStary.visibility = View.GONE
            btnfineshdelivery.visibility = View.VISIBLE

            // on order delivered

            Log.i("asdf", "orderid==" + ordersid);

            val myApplicationInterface =
                AppController.instance?.requestQueue?.create(UserService::class.java)
            val call = myApplicationInterface?.getDriverStartWaterDeliveryResponse(
                "application/json",
                DriverStartWaterDeliveryRequest(ordersid)
            )
            call?.enqueue(object : Callback<DriverStartWaterDeliveryResponse> {
                override fun onFailure(call: Call<DriverStartWaterDeliveryResponse>, t: Throwable) {

                    Toast.makeText(
                        this@ActivityStartWaterDelivery,
                        "Something Went Wrong!!",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onResponse(
                    call: Call<DriverStartWaterDeliveryResponse>,
                    response: Response<DriverStartWaterDeliveryResponse>
                ) {
                    val orderDeliveredDriverResponse = response.body()
                    Log.i("asdf", "ordercomplete====first==" + response.body());
                    if (orderDeliveredDriverResponse?.status!!) {
                        Toast.makeText(
                            this@ActivityStartWaterDelivery,
                            orderDeliveredDriverResponse.message,
                            Toast.LENGTH_SHORT
                        ).show()

                        Log.i("asdf", "ordercomplete===secound===" + response.body());


                        sharedPref!!.edit().putBoolean(
                            "orderStartDeliverystatus",
                            orderDeliveredDriverResponse.status
                        )
                            .apply()


                        //  setupTheMapForDriverLocation(LAT,LONG);

                    } else {
                        Toast.makeText(
                            this@ActivityStartWaterDelivery,
                            orderDeliveredDriverResponse.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                }
            })

        }
        //cancel button click of custom layout
        mDialogView.NoReachLocation.setOnClickListener {
            //dismiss dialog
            mAlertDialog.dismiss()
        }
    }

    private fun popupFineshWaterDelivery() {
        //Inflate the dialog with custom view
        val mDialogView =
            LayoutInflater.from(this).inflate(R.layout.dailog_fineshwatercelivery, null)
        //AlertDialogBuilder
        val mBuilder = AlertDialog.Builder(this)
            .setView(mDialogView)
        //show dialog
        val mAlertDialog = mBuilder.show()
        //login button click of custom layout
        mDialogView.YesReachLocation.setOnClickListener {
            mAlertDialog.dismiss()

            Toast.makeText(applicationContext, "Finish", Toast.LENGTH_SHORT).show()

            val myApplicationInterface =
                AppController.instance?.requestQueue?.create(UserService::class.java)
            val call = myApplicationInterface?.getOrderDriverResponse(
                "application/json",
                OrderDeliveredDriverRequest(ordersid, 3)
            )
            call?.enqueue(object : Callback<OrderDeliveredDriverResponse> {
                override fun onFailure(call: Call<OrderDeliveredDriverResponse>, t: Throwable) {

                    Toast.makeText(
                        this@ActivityStartWaterDelivery,
                        "Something Went Wrong!!",
                        Toast.LENGTH_SHORT
                    ).show()

                }

                override fun onResponse(
                    call: Call<OrderDeliveredDriverResponse>,
                    response: Response<OrderDeliveredDriverResponse>
                ) {
                    try {
                        if (response.body() != null) {
                            val orderDeliveredDriverResponse = response.body()
                            Log.i("asdf", "ordercomplete====first==" + response.body());
                            if (orderDeliveredDriverResponse?.status != null) {
                                if (orderDeliveredDriverResponse?.status!!) {
                                    Toast.makeText(
                                        this@ActivityStartWaterDelivery,
                                        orderDeliveredDriverResponse.message,
                                        Toast.LENGTH_SHORT
                                    ).show()

                                    Log.i("asdf", "ordercomplete===secound===" + response.body());



                                    rellayReachLoc.visibility = View.GONE
                                    laybillDetails.visibility = View.VISIBLE

                                    setTitle("Bill Details")

                                    DriverLat = sharedPref!!.getString("DriverLat", "").toDouble()
                                    DriverLong = sharedPref!!.getString("DriverLong", "").toDouble()


                                    customerLat =
                                        sharedPref!!.getString("customerlat", "").toDouble()
                                    customerLong =
                                        sharedPref!!.getString("customerlong", "").toDouble()

                                    txtDistanceTraveled.setText(
                                        DecimalFormat("##.###").format(
                                            calculateDistance()
                                        ) + " KM"
                                    )
                                    tankerType.text = sharedPref!!.getString("tankerType", "")
                                    delivery_amount.text =
                                        "Rs " + sharedPref!!.getString("amount", "0")

                                    discount.text = "Rs " + sharedPref!!.getString("discount", "40")



                                    Log.i(
                                        "fds",
                                        "fsfsdfsdfxcvdsd======" + sharedPref!!.getString(
                                            "customerpaymentmode",
                                            ""
                                        )
                                    )
                                    if (sharedPref!!.getString(
                                            "customerpaymentmode",
                                            ""
                                        ).equals("online")
                                    ) {
                                        netAmount.text = "₹ " + "0.00"
                                    } else {
                                        netAmount.text = "₹ " + (sharedPref?.getString(
                                            "amount",
                                            "0"
                                        )!!.toFloat() - sharedPref?.getString(
                                            "discount",
                                            "0"
                                        )!!.toFloat()).toString()
                                    }

                                    // create bill activity

                                    fineshbill = 1

                                    val cancelorder = "orderfinesh"
                                    val result = Intent()
                                    result.putExtra("orderStatus", cancelorder)
                                    result.putExtra("orderid", ordersid)

                                    setResult(11, result)

                                    sharedPref!!.edit().clear().commit()



                                    SetStatusOfTankerMan(0)
                                    //  setupTheMapForDriverLocation(LAT,LONG);

                                } else {
                                    Toast.makeText(
                                        this@ActivityStartWaterDelivery,
                                        orderDeliveredDriverResponse.message,
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            })

        }
        //cancel button click of custom layout
        mDialogView.NoReachLocation.setOnClickListener {
            //dismiss dialog
            mAlertDialog.dismiss()
        }
    }

    private fun calculateDistance(): Double {
        val Radius = 6371// radius of earth in Km
        Log.i("sadf", "the radius is d ====${DriverLat}")
        Log.i("sadf", "the radius is d====${DriverLong}")
        Log.i("sadf", "the radius is  c ====${customerLat}")
        Log.i("sadf", "the radius is  c====${customerLong}")
        var lat1: Double = DriverLat as Double
        var lat2: Double = customerLat as Double
        var lon1: Double = DriverLong as Double
        var lon2: Double = customerLong as Double
        var dLat: Double = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)
        val a = (Math.sin(dLat / 2) * Math.sin(dLat / 2) + (Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2)
                * Math.sin(dLon / 2)))
        val c = 2 * Math.asin(Math.sqrt(a))
        val valueResult = Radius * c
        val km = valueResult / 1
        val newFormat = DecimalFormat("####")
        val kmInDec = Integer.valueOf(newFormat.format(km))
        val meter = valueResult % 1000
        val meterInDec = Integer.valueOf(newFormat.format(meter))
        Log.i(
            "Radius Value", ("" + valueResult + " KM " + kmInDec
                    + " Meter " + meterInDec)
        )
        //Toast.makeText(ActivityStartWaterDelivery@this,"the distance===="+(Radius*c),Toast.LENGTH_LONG);
        Log.i("asdsdf", "the data is ===" + Radius * c)

        return Radius * c


    }


    private fun getCancellationReasons() {

        val myApplicationInterface =
            AppController.instance?.requestQueue?.create(UserService::class.java)
        val call = myApplicationInterface?.getCancelOrderResponse("application/json")
        call?.enqueue(object : Callback<CancelOrderResponse> {
            override fun onFailure(call: Call<CancelOrderResponse>, t: Throwable) {
                Toast.makeText(
                    this@ActivityStartWaterDelivery,
                    "Something Went Wrong!!",
                    Toast.LENGTH_SHORT
                ).show()
            }

            override fun onResponse(
                call: Call<CancelOrderResponse>,
                response: Response<CancelOrderResponse>
            ) {

                val cancelOrderResponse = response.body()


                Log.i("asdf", "cancel resion is is is ====" + response.body())

                try {
                    if (cancelOrderResponse?.status != null) {
                        if (cancelOrderResponse?.status!!) {
                            res = cancelOrderResponse.cancelReasons

                            for (reasonlist in res!!) {
                                reasonList = reasonList.plus(reasonlist.name)
                            }
                            //  Toast.makeText(this@Home, reasonList[2], Toast.LENGTH_SHORT).show()
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }


            }
        })

    }

    private fun CancelTheCurrentOrder(orderid: String, resionid: String, deiverby: String) {

        AlertUtils.showCustomProgressDialog(this@ActivityStartWaterDelivery)

        val myApplicationInterface =
            AppController.instance?.requestQueue?.create(UserService::class.java)
        val call = myApplicationInterface?.getCurrentCancelOrderResponse(
            "application/json",

            CurrentCancelOrderRequest(deiverby?.toInt(), orderid, resionid?.toInt())

        )


        call?.enqueue(object : Callback<CurrentCancelOrderResponse> {
            override fun onFailure(call: Call<CurrentCancelOrderResponse>, t: Throwable) {
                AlertUtils.dismissDialog()
                Toast.makeText(
                    this@ActivityStartWaterDelivery,
                    "Something Went Wrong!!",
                    Toast.LENGTH_SHORT
                ).show()
            }

            override fun onResponse(
                call: Call<CurrentCancelOrderResponse>,
                response: Response<CurrentCancelOrderResponse>
            ) {

                AlertUtils.dismissDialog()

                val currentCancelResponse = response.body()

                Log.i("sadf", "canceloesion======" + response.body());
                try {
                    if (currentCancelResponse?.status != null) {
                        if (currentCancelResponse?.status!!) {

                            sharedPref!!.edit().clear().commit()

                            val cancelorder = "ordercancel"
                            val result = Intent()
                            result.putExtra("orderStatus", cancelorder)
                            setResult(11, result)
                            finish()
                        }

                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }
        })
    }


    /*  override fun onSupportNavigateUp(): Boolean {

          val cancelorder = "orderBlank"
          val result = Intent()
          result.putExtra("orderStatus", cancelorder)
          setResult(11, result)

          onBackPressed()
          return true
      }*/


    override fun onSupportNavigateUp(): Boolean {
        handlerforCancel.removeMessages(0)


        if (fineshbill == 1) {
            finish()
        } else {
            val cancelorder = "orderDeliveryStart"
            val result = Intent()
            result.putExtra("orderStatus", cancelorder)
            setResult(11, result)

            finish()
        }


        return true
    }


    private fun doCancelCheck() {

        handlerforCancel.postDelayed({


            val myApplicationInterface =
                AppController.instance?.requestQueue?.create(UserService::class.java)
            val call = myApplicationInterface?.getOrderDetail(
                "application/json",
                OrderDetailRequest(ordersid)
            )
            call?.enqueue(object : Callback<OrderDetailResponse> {
                override fun onFailure(call: Call<OrderDetailResponse>, t: Throwable) {

                    Toast.makeText(
                        this@ActivityStartWaterDelivery,
                        "Something Went Wrong!!",
                        Toast.LENGTH_SHORT
                    ).show()

                }

                override fun onResponse(
                    call: Call<OrderDetailResponse>,
                    response: Response<OrderDetailResponse>
                ) {
                    try {
                        if (response.body() != null) {
                            val orderDetailResponse = response.body()

                            var can: Int = 0
                            can = orderDetailResponse!!.order_detailss.cancel_by

                            Log.i("asdf", "this is null =============" + can)


                            if (can == 0) {
                                doCancelCheck()
                            } else if (can == UserShared.getUserID(this@ActivityStartWaterDelivery)!!.toInt()) {
                                handlerforCancel.removeMessages(0)
                                sharedPref!!.edit().clear().apply()

                                val intent = Intent(applicationContext, Home::class.java)
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                                startActivity(intent)


                                handlerforCancel.removeMessages(0)

                            } else {

                                handlerforCancel.removeMessages(0)

                                if (!((this@ActivityStartWaterDelivery)).isFinishing()) {
                                    val mDialogView =
                                        LayoutInflater.from(this@ActivityStartWaterDelivery)
                                            .inflate(R.layout.dialog_cancel_by_customer, null)
                                    val mBuilder = AlertDialog.Builder(this@ActivityStartWaterDelivery)
                                        .setView(mDialogView)

                                    val mAlertDialog = mBuilder.show()
                                    mAlertDialog.OKButton.setOnClickListener {
                                        mAlertDialog.dismiss()
                                        sharedPref!!.edit().clear().apply()

                                        val intent = Intent(applicationContext, Home::class.java)
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                                        startActivity(intent)

                                    }

                                    mAlertDialog.setOnDismissListener {
                                        sharedPref!!.edit().clear().commit()
                                        val intent = Intent(applicationContext, Home::class.java)
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                                        startActivity(intent)
                                    }

                                }


                            }


                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            })


        }, 10000)


    }

    override fun onBackPressed() {
        if (fineshbill == 1) {
            finish()
        } else {
            val cancelorder = "orderDeliveryStart"
            val result = Intent()
            result.putExtra("orderStatus", cancelorder)
            setResult(11, result)

            finish()
        }
    }

    private fun SetStatusOfTankerMan(status: Int) {

        AlertUtils.showCustomProgressDialog(this@ActivityStartWaterDelivery)


        val myApplicationInterface =
            AppController.instance?.requestQueue?.create(UserService::class.java)
        val call = myApplicationInterface?.getTankerStatusResponse(
            "application/json",
            TankerStatusRequest(status, UserShared.getUserID(this@ActivityStartWaterDelivery)!!)
        )
        call?.enqueue(object : Callback<TankerStatusResponse> {
            override fun onFailure(call: Call<TankerStatusResponse>, t: Throwable) {
                AlertUtils.dismissDialog()
                Toast.makeText(
                    this@ActivityStartWaterDelivery,
                    "Something Went Wrong!",
                    Toast.LENGTH_SHORT
                ).show()
            }

            override fun onResponse(
                call: Call<TankerStatusResponse>,
                response: Response<TankerStatusResponse>
            ) {
                AlertUtils.dismissDialog()
                val responseTank = response.body()
                if (responseTank?.status!!) {
                    Toast.makeText(
                        this@ActivityStartWaterDelivery,
                        responseTank.message,
                        Toast.LENGTH_SHORT
                    ).show()
                    if (status == 1) {

                    } else {

                    }
                }


            }
        })
    }


}