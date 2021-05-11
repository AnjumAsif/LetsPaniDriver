package com.example.letspaanidriver.activity

/*import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode*/

import android.Manifest
import android.annotation.SuppressLint
import android.content.*
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.location.*
import android.location.LocationListener
import android.location.LocationManager.GPS_PROVIDER
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.text.TextUtils
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.multidex.MultiDex
import com.akexorcist.googledirection.DirectionCallback
import com.akexorcist.googledirection.GoogleDirection
import com.akexorcist.googledirection.constant.TransportMode
import com.akexorcist.googledirection.model.Direction
import com.akexorcist.googledirection.model.Route
import com.akexorcist.googledirection.util.DirectionConverter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.letspaanidriver.R
import com.example.letspaanidriver.apis.classes.*
import com.example.letspaanidriver.utils.FloatingViewService
import com.example.letspaanidriver.utils.LocationService
import com.example.letspani.apis.AppController
import com.example.letspani.apis.UserService
import com.example.letspani.apis.classes.RatingRequest
import com.example.letspani.apis.classes.RatingResponse
import com.example.letspani.utils.AlertUtils
import com.example.letspani.utils.UserShared
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.ResultCallback
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.model.*
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.activity_home2.*
import kotlinx.android.synthetic.main.change_language.view.*
import kotlinx.android.synthetic.main.dailog_giveratingtotankerman.*
import kotlinx.android.synthetic.main.dailog_location_reach_conformation.view.*
import kotlinx.android.synthetic.main.dialog_cancel_by_customer.*
import kotlinx.android.synthetic.main.nav_header_home.*
import kotlinx.android.synthetic.main.nav_header_home.view.*
import org.jetbrains.anko.textColor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.DecimalFormat
import java.util.*

class Home : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener,
    DirectionCallback,
    GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
    com.google.android.gms.location.LocationListener {
    private lateinit var mGoogleApiClient: GoogleApiClient
    private var mLocationManager: LocationManager? = null
    lateinit var mLocation: Location
    private var mLocationRequest: LocationRequest? = null
    private val listener: com.google.android.gms.location.LocationListener? = null
    private val UPDATE_INTERVAL = (2 * 1000).toLong()  /* 10 secs */
    private val FASTEST_INTERVAL: Long = 2000 /* 2 sec */
    var DriverLat: Double? = null
    var DriverLong: Double? = null
    private var status = 0
    private var PRIVATE_MODE = 0
    var customerLat: Double? = null
    var customerLong: Double? = null
    var handlerforCancel = Handler()
    private var cancelBy = ""
    val updateLiveLocation = Handler(Looper.getMainLooper())
    val mainHandler = Handler(Looper.getMainLooper())
    private val SYSTEM_ALERT_WINDOW_PERMISSION = 2084
    private val PLACE_PICKER_REQUEST = 999
    private var locationManager: LocationManager? = null
    internal var location: Location? = null
    internal var permissionsRequired = arrayOf(
        android.Manifest.permission.ACCESS_COARSE_LOCATION,
        android.Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.INTERNET
    )
    internal var lat: Double? = null
    internal var longi: Double? = null
    internal var longitude: Double? = null
    internal var latitude: Double? = null
    private var googleApiClient: GoogleApiClient? = null
    internal val REQUEST_LOCATION = 199
    var LAT: String = ""
    var LONG: String = ""
    var LOCALITY: String = ""
    var handlerforLive = Handler()
    var handlerforOrder = Handler()
    val checkorderHandler = Handler(Looper.getMainLooper())
    private var ordersid = ""
    private var customeraddress = ""
    private var customername = ""
    private var customerpaymentmode = ""
    private var deliverytime = ""
    private var phonenumber = ""
    private var customer_id = ""
    private var delivery_address = ""
    private var house_number = ""
    private var user_address_text = ""
    var sharedPref: SharedPreferences? = null
    //private var reasonList: Array<String> = arrayOf("Order Placed By Mistake"
    private var reasonList: Array<String> = arrayOf()
    var res: List<CancelOrderResponse.CancelReason>? = null
    //mycode
    private var origin: LatLng? = null
    private var destination: LatLng? = null
    var googleMap: GoogleMap? = null
    val serverKey: String = "AIzaSyCYwlqPDipbyNoR18dWmUxBZRN8YWTC2ow"
    val TASK_REQUEST: Int = 11

    companion object {
        var langval = 0
        var check = 0
        var WebViewType: String = ""
        private const val PERMISSION_REQUEST_ACCESS_FINE_LOCATION = 100
    }

    private var orderDeliveryStatus = false
    private var orderReachStatus = false
    var sharedPrefLanguage: SharedPreferences? = null
    @SuppressLint("WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPrefLanguage = getSharedPreferences("language", PRIVATE_MODE)
        langval = sharedPrefLanguage!!.getInt("lang", 0)
        /* if (langval == 1) {
             check == 1
             val locale = Locale("hi")
             Locale.setDefault(locale)
             val config = Configuration()
             config.locale = locale
             baseContext.resources.updateConfiguration(
                 config,
                 baseContext.resources.displayMetrics
             )
             setContentView(R.layout.activity_home2)
         } else {
             setContentView(R.layout.activity_home2)
         }
 */
        val userid = UserShared.getUserID(this@Home)
        Log.i("jhjfdjfg", userid)
        if (langval == 1) {
            val locale = Locale("hi")
            Locale.setDefault(locale)
            val config = Configuration()
            config.locale = locale
            baseContext.resources.updateConfiguration(
                config,
                baseContext.resources.displayMetrics
            )
            setContentView(R.layout.activity_home2)
        }
        if (langval == 0) {
            val locale = Locale("en")
            Locale.setDefault(locale)
            val config = Configuration()
            config.locale = locale
            baseContext.resources.updateConfiguration(
                config,
                baseContext.resources.displayMetrics
            )
            setContentView(R.layout.activity_home2)
        } else {
            setContentView(R.layout.activity_home2)
        }

        nav_view.setNavigationItemSelectedListener(this)
        sharedPref = getSharedPreferences("OrderDetail", PRIVATE_MODE)
        AlertUtils.showCustomProgressDialog(this@Home)
        MultiDex.install(this)
        mGoogleApiClient = GoogleApiClient.Builder(this)
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this)
            .addApi(LocationServices.API)
            .build()
        mLocationManager = this.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        with(mapView) {
            // Initialise the MapView
            onCreate(null)
            // Set the map ready callback to receive the GoogleMap object
            getMapAsync {
                MapsInitializer.initialize(applicationContext)
                setMapLocation(it)
            }
        }
        nav_view.getHeaderView(0).Layout.setOnClickListener {
            val intent = Intent(applicationContext, BasicActivity::class.java)
            intent.putExtra("frag", "c")
            startActivityForResult(intent, 9)
        }
        nav_view.getHeaderView(0).Name.text = UserShared.getUserName(this@Home)
        if (UserShared.getUserPic(this@Home)!!.isNotEmpty()) {
            Glide.with(this@Home).load(UserShared.getUserPic(this@Home)!!)
                .into(nav_view.getHeaderView(0).pic);
            Glide.with(this@Home)
                .load(UserShared.getUserPic(this@Home))
                .apply(RequestOptions.skipMemoryCacheOf(true))
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
                .into(nav_view.getHeaderView(0).pic);
            //  ImageLoader.getInstance().displayImage(UserShared.getUserPic(this@Home)!!, nav_view.getHeaderView(0).pic)
        } else {
            nav_view.getHeaderView(0).pic.setImageResource(R.drawable.default_pic)
        }
        with(mapView) {
            // Initialise the MapView
            onCreate(null)
            // Set the map ready callback to receive the GoogleMap object
            getMapAsync {
                MapsInitializer.initialize(applicationContext)
                setMapLocation(it)
                googleMap = it
            }
        }
        navigation_icon.setOnClickListener {
            drawer_layout.openDrawer(Gravity.START)

        }
        ActiveSwitch.setOnToggledListener { toggleableView, isOn ->
            Log.i("asdf", "isonline==========" + isOn);
            if (isOn) {
                SetStatusOfTankerMan(1)
                layNavigateCancel.visibility = View.GONE
                userAddress.visibility = View.GONE
            } else {
                SetStatusOfTankerMan(0)
            }
            GetCurrentLatLong()
        }
        /*  val iscancel= sharedPrefCancel!!.getBoolean("cancel",false);

          if(iscancel)
          {


              sharedPrefCancel!!.edit().clear().commit();
          }*/



        GetCurrentLatLong()

        CheckLocationPernission()


        if (LAT.isNotEmpty()) {
            doTheAutoRefresh()
        } else {
            Toast.makeText(this@Home, "Can't Get Your Location", Toast.LENGTH_SHORT).show()
        }
        // doTheAutoRefreshForCheckingOrder()
        // askPermission()

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            // startService(Intent(this@Home, FloatingViewService::class.java))
            // finish();
        } else if (Settings.canDrawOverlays(this)) {
            // startService(Intent(this@Home, FloatingViewService::class.java))
            // finish();
        } else {
            askPermission()
            Toast.makeText(
                this,
                "You need System Alert Window Permission to do this",
                Toast.LENGTH_SHORT
            ).show()
        }
        btnNavigator.setOnClickListener {

            val uri =
                "http://maps.google.com/maps?saddr=" + LAT + "," + LONG + "&daddr=" + customerLat + "," + customerLong
            val intent = Intent(android.content.Intent.ACTION_VIEW, Uri.parse(uri))
            intent.setClassName(
                "com.google.android.apps.maps",
                "com.google.android.maps.MapsActivity"
            )
            startActivityForResult(intent, 1)

            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {

                val i = Intent(this@Home, FloatingViewService::class.java)
                startService(i)


            } else if (Settings.canDrawOverlays(this)) {

                val i = Intent(this@Home, FloatingViewService::class.java)
                startService(i)

            } else {
                askPermission()
                Toast.makeText(
                    this,
                    "You need System Alert Window Permission to do this",
                    Toast.LENGTH_SHORT
                ).show()
            }
            val i = Intent(this@Home, LocationService::class.java)
            startService(i)
        }


        btnDelivery.setOnClickListener {
            ShowAlertOrderComplete()

        }



        findMe.setOnClickListener {
            if (status == 0) {
                GetCurrentLatLong()

                setMapLocation(googleMap!!)
            }
        }

        getDriverRatings()
        checkLocation()
        checkIsDriverOnline()

        AlertUtils.dismissDialog()
    }

    private fun checkIsDriverOnline() {
        val userid = UserShared.getUserID(this@Home)
        val myApplicationInterface =
            AppController.instance?.requestQueue?.create(UserService::class.java)
        val call = myApplicationInterface?.getDriverProfile(
            "application/json",
            ProfileRequest(userid.toString())
        )
        call?.enqueue(object : Callback<ProfileResponse> {
            override fun onFailure(call: Call<ProfileResponse>, t: Throwable) {
                                Toast.makeText(this@Home, "Something Went Wrong!!", Toast.LENGTH_SHORT).show()

            }

            override fun onResponse(
                call: Call<ProfileResponse>,
                response: Response<ProfileResponse>
            ) {
                val profileResponse = response.body()

                Log.i("tag", "the output is==" + response.body())

                if (profileResponse?.status!!) {

                    profileResponse.profileData.isOnline

                    if (profileResponse.profileData.isOnline == 0) {

                        ActiveSwitch.setOn(false)
                        StatusOfDriver.text = getString(R.string.inac)
                        StatusOfDriver.textColor = resources.getColor(R.color.white)
                        ActiveSwitch.labelOff = getString(R.string.off)


                        layNavigateCancel.visibility = View.GONE
                        userAddress.visibility = View.GONE

                        // Toast.makeText(this@Home,"You are offline",Toast.LENGTH_SHORT).show()

                        AlertUtils.dismissDialog()

                    } else {

                        ActiveSwitch.setOn(true)
                        StatusOfDriver.text = getString(R.string.active)
                        ActiveSwitch.labelOn = getString(R.string.on)


                        // Toast.makeText(this@Home,"You are online",Toast.LENGTH_SHORT).show()

                        StatusOfDriver.textColor = resources.getColor(R.color.colorAccent)

                        val orderActive = sharedPref!!.getString("orderid", "")

                        Log.i("asdf", "activeid=======" + orderActive)

                        if (orderActive != "") {

                            Log.i(
                                "asdf",
                                "orderactivity===" + sharedPref!!.getString("orderid", "")
                            )

                            ordersid = sharedPref!!.getString("orderid", "")
                            customeraddress = sharedPref!!.getString("customeraddress", "")
                            customerLat = sharedPref!!.getString("customerlat", "").toDouble()
                            customerLong = sharedPref!!.getString("customerlong", "").toDouble()
                            customername = sharedPref!!.getString("customername", "")
                            customerpaymentmode = sharedPref!!.getString("customerpaymentmode", "")
                            deliverytime = sharedPref!!.getString("deliverytime", "")
                            phonenumber = sharedPref!!.getString("phonenumber", "")
                            customer_id = sharedPref!!.getString("customerid", "")
                            delivery_address =
                                sharedPref!!.getString("customerdelivery_address", "")

                            house_number = sharedPref!!.getString("house_no", "")
                            user_address_text = sharedPref!!.getString("customeraddtext", "")

                            var house = ""
                            if (house_number === "null" || house_number == null) {


                                house = ""
                            } else {
                                house = house_number
                            }
                            DeliverToHouseNo.text = house
                            DeliveryLocationAddress.text = user_address_text
                            Log.i("Dsfsd", "dfsdfsdfsd===${user_address_text}")


                            if (customerLat != null || customerLong != null) {
                                setupTheMapForDriverLocation()
                            } else {
                                convertAddress(customeraddress)
                            }


                            doCancelCheck()


                            orderReachStatus =
                                sharedPref!!.getBoolean("orderReachLocationstatus", false)
                            if (orderReachStatus) {
                                layNavigateCancel.visibility = View.GONE
                                userAddress.visibility = View.GONE
                                startwaterdelivery.visibility = View.VISIBLE
                                btnDelivery.visibility = View.GONE
                                startwaterdelivery.setOnClickListener {
                                    val intent = Intent(
                                        applicationContext,
                                        ActivityStartWaterDelivery::class.java
                                    )
                                    startActivityForResult(intent, TASK_REQUEST)
                                }

                            }


                            orderDeliveryStatus =
                                sharedPref!!.getBoolean("orderStartDeliverystatus", false)
                            if (orderDeliveryStatus) {
                                layNavigateCancel.visibility = View.GONE
                                userAddress.visibility = View.GONE
                                finishDelivery.visibility = View.VISIBLE

                                btnDelivery.visibility = View.GONE
                                startwaterdelivery.visibility = View.GONE

                                finishDelivery.setOnClickListener {
                                    val intent = Intent(
                                        applicationContext,
                                        ActivityStartWaterDelivery::class.java
                                    )
                                    startActivityForResult(intent, TASK_REQUEST)
                                }

                            }

                            //  setupTheMapForDriverLocation();

                            Log.i("activeuser", "activeuser=" + ordersid)
                            Log.i("activeuser", "activeuser=" + customername)
                            Log.i("activeuser", "activeuser=" + customeraddress)
                            Log.i("activeuser", "activeuser=" + customerpaymentmode)
                            Log.i("activeuser", "activeuser=" + deliverytime)
                            Log.i("activeuser", "activeuser=" + phonenumber)

                        } else {


                            layNavigateCancel.visibility = View.GONE
                            userAddress.visibility = View.GONE
                            CancelOrCompleteDelivery()
                            AlertUtils.dismissDialog()
                        }


                    }

                }
            }


        })

    }

    private fun CancelOrCompleteDelivery() {
        mainHandler.post(object : Runnable {
            override fun run() {
                ShowAlertOrder()

                Log.i("asdf", "logmethodruncontinueslly")
                mainHandler.postDelayed(this, 4000)

            }
        })
    }


    private fun CheckLocationPernission() {
        if (longitude != null) {
            SendDriverLocation()
        }
//        val i = Intent(this@Home, LocationService::class.java)
//        startService(i)
    }

    private fun OrderAcceptedOrNot(orderid: String) {
        checkorderHandler.post(object : Runnable {
            override fun run() {
                CheckOrderAcceptedOrNot(orderid)

                checkorderHandler.postDelayed(this, 3000)


            }
        })
    }


    private fun SendDriverLocation() {
        GetCurrentLatLong()
        val myApplicationInterface =
            AppController.instance?.requestQueue?.create(UserService::class.java)
        val call = myApplicationInterface?.getDriverLiveResponse(
            "application/json",
            LiveLocationRequest(
                longitude.toString(),
                latitude.toString(),
                LOCALITY,
                UserShared.getUserID(this@Home)!!
            )
        )
        call?.enqueue(object : Callback<LiveLocationResponse> {
            override fun onFailure(call: Call<LiveLocationResponse>, t: Throwable) {
//                Toast.makeText(this@Home, "Something Went Wrong!!", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(
                call: Call<LiveLocationResponse>,
                response: Response<LiveLocationResponse>
            ) {
                try {
                    if (response.body() != null) {

                        val liveLocationResponse = response.body()

                        Log.i("asdf", "response====")

                        if (liveLocationResponse?.status!!) {
                            Toast.makeText(
                                this@Home,
                                liveLocationResponse.message,
                                Toast.LENGTH_SHORT
                            )
                                .show()
                            Log.i(
                                "tag",
                                "the dtring data show========" + liveLocationResponse.message
                            )

                            //  setupTheMapForDriverLocation(LAT,LONG);

                        } else {
                            Log.i(
                                "tag",
                                "the dtring data show========${liveLocationResponse.message}"
                            )
                            Toast.makeText(
                                this@Home,
                                liveLocationResponse.message,
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }
        })
    }

    private fun GetAddressFromLatLang(lat: String, long: String) {

        try {
            val geocoder = Geocoder(this@Home, Locale.getDefault())
            val addressList: List<Address> =
                geocoder.getFromLocation(lat.toDouble(), long.toDouble(), 1)
            if (addressList.isNotEmpty()) {
                LOCALITY = addressList[0].locality
            }
        } catch (e: Exception) {

        } // end catch
    } // end if


    private fun doTheAutoRefresh() {
        handlerforLive.postDelayed({
            doTheAutoRefresh()
            //My Stuff
            if (longitude != null) {
                SendDriverLocation()
            }

            CheckLocationPernission()
        }, 15000)
    }

/*    private fun doTheAutoRefreshForCheckingOrder() {

        handlerforOrder.postDelayed({
            doTheAutoRefreshForCheckingOrder()
            //My Stuff

            ShowAlertOrder()

        }, 4000)
}*/


    // Google map diraction

    private fun setupTheMapForDriverLocation() {

        // origin = LatLng(lat.toDouble(), lang.toDouble())

        val latit = AppController.instance!!.driverlocationLatitude
        val longi = AppController.instance!!.driverlocationLongitude

        Log.i("location", "driverlive location====latitude===" + latit + "longitude" + longi)

        // origin = LatLng(latit!!.toDouble(), longi!!.toDouble())

        if (LAT != "" && LONG != "") {
            origin = LatLng(LAT.toDouble(), LONG.toDouble())

            destination = LatLng(customerLat!!.toDouble(), customerLong!!.toDouble())

            GoogleDirection.withServerKey(serverKey)
                .from(origin)
                .to(destination)
                .transportMode(TransportMode.DRIVING)
                .execute(this)
            layNavigateCancel.visibility = View.VISIBLE
            userAddress.visibility = View.VISIBLE

            // calculate distance
            val latLngA = LatLng(LAT.toDouble(), LONG.toDouble())
            val latLngB = LatLng(customerLat!!.toDouble(), customerLong!!.toDouble())

            val locationA = Location("point A")
            locationA.latitude = latLngA.latitude
            locationA.longitude = latLngA.longitude
            val locationB = Location("point B")
            locationB.latitude = latLngB.latitude
            locationB.longitude = latLngB.longitude

            val distance = locationA.distanceTo(locationB)

            // distance=70
            Log.i("location", "distance=========" + distance)

            if (distance <= 1000 || distance <= 500 || distance <= 100) {
                if (sharedPref!!.getBoolean("orderStartDeliverystatus", false) == false) {
//                    ShowAlertOrderComplete()
                }
            }


        } else {
            val handler = Handler()
            handler.postDelayed(object : Runnable {
                override fun run() {
                    setupTheMapForDriverLocation()
                }
            }, 1000)
        }
        getCancellationReasons()


        TankerCancel.setOnClickListener {

            val reasons = reasonList

            var resionid = ""

            val alt_builder = AlertDialog.Builder(this@Home)
            alt_builder.setTitle("Select a Cancelation Reason")
            alt_builder.setSingleChoiceItems(reasons, -1, DialogInterface.OnClickListener
            { dialogInterface, i ->

                resionid = res!!.get(i).id.toString()
                Toast.makeText(this@Home, reasons[i], Toast.LENGTH_SHORT).show()
                AlertUtils.dialog?.dismiss()

                Log.i("asdf", "cancelresion==" + res!!.get(i).id)
            })

            alt_builder.setPositiveButton(
                "Ok",
                DialogInterface.OnClickListener { dialogInterface, i ->


                    dialogInterface.dismiss()
                    if (!TextUtils.isEmpty(resionid)) {
                        dialogInterface.dismiss()
                        CancelTheCurrentOrder(
                            ordersid,
                            resionid,
                            UserShared.getUserID(applicationContext).toString()
                        )
                    } else {
                        Toast.makeText(
                            this@Home,
                            "Please select any one reason for cancellation",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }

                })

            alt_builder.setNegativeButton(
                "Cancel",
                DialogInterface.OnClickListener { dialogInterface, i ->

                    dialogInterface.dismiss()
                })

            val alert = alt_builder.create()
            alert.show()

        }


    }

    private fun getCancellationReasons() {

        val myApplicationInterface =
            AppController.instance?.requestQueue?.create(UserService::class.java)
        val call = myApplicationInterface?.getCancelOrderResponse("application/json")
        call?.enqueue(object : Callback<CancelOrderResponse> {
            override fun onFailure(call: Call<CancelOrderResponse>, t: Throwable) {
                Toast.makeText(this@Home, "Something Went Wrong!!", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(
                call: Call<CancelOrderResponse>,
                response: Response<CancelOrderResponse>
            ) {

                val cancelOrderResponse = response.body()

                Log.i("asdf", "cancel resion is is is ====" + response.body())
                try {
                    if (cancelOrderResponse?.status != null) {
                        reasonList = emptyArray()

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

        AlertUtils.showCustomProgressDialog(this@Home)

        val myApplicationInterface =
            AppController.instance?.requestQueue?.create(UserService::class.java)
        val call = myApplicationInterface?.getCurrentCancelOrderResponse(
            "application/json",

            CurrentCancelOrderRequest(deiverby.toInt(), orderid, resionid.toInt())

        )


        call?.enqueue(object : Callback<CurrentCancelOrderResponse> {
            override fun onFailure(call: Call<CurrentCancelOrderResponse>, t: Throwable) {
                AlertUtils.dismissDialog()
                Toast.makeText(this@Home, "Something Went Wrong!!", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(
                call: Call<CurrentCancelOrderResponse>,
                response: Response<CurrentCancelOrderResponse>
            ) {

                AlertUtils.dismissDialog()

                val currentCancelResponse = response.body()


                Log.i("sadf", "canceloesion======" + response.body())

                try {
                    if (currentCancelResponse?.status != null) {
                        if (currentCancelResponse?.status!!) {

                            sharedPref!!.edit().clear().commit()

                            ordersid = ""
                            customeraddress = ""
                            customerLat = null
                            customerLong = null
                            customername = ""
                            customerpaymentmode = ""
                            deliverytime = ""
                            phonenumber = ""
                            customer_id = ""
                            delivery_address = ""

                            handlerforCancel.removeMessages(0)


                            val intent = Intent(applicationContext, Home::class.java)
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                            startActivity(intent)


                            /* if (googleMap != null) {
                                 setMapLocation(googleMap!!)
                             }*/
/*
                    if (googleMap != null) {
                        setMapLocation(googleMap!!)
                    }*/

                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }


            }
        })

    }

    //Setting Up the Map
    private fun setMapLoca() {
        with(googleMap) {
            val lat = LAT
            //   if (lat.isNotEmpty()) {
            this?.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(26.8505, 75.7628), 15f))
            this?.addMarker(
                MarkerOptions().position(LatLng(26.8505, 75.7628)).title("Your Location").icon(
                    BitmapDescriptorFactory.fromResource(R.drawable.drop_location)
                )
            )
            this!!.mapType = GoogleMap.MAP_TYPE_NORMAL

            //  }
        }
    }


    /*    private double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1))
                        * Math.sin(deg2rad(lat2))
                        + Math.cos(deg2rad(lat1))
                        * Math.cos(deg2rad(lat2))
                        * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        return (dist);
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }*/
    private fun askPermission() {
        val intent = Intent(
            Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
            Uri.parse("package:$packageName")

        )
        startActivityForResult(intent, SYSTEM_ALERT_WINDOW_PERMISSION)
    }

    /*override fun onClick(v: View) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            startService(Intent(this@Home, FloatingViewService::class.java))
            // finish();
        } else if (Settings.canDrawOverlays(this)) {
            startService(Intent(this@Home, FloatingViewService::class.java))
            // finish();
        } else {
            askPermission()
            Toast.makeText(this, "You need System Alert Window Permission to do this", Toast.LENGTH_SHORT).show()
        }
    }*/

    override fun onDirectionSuccess(direction: Direction?, rawBody: String?) {
        var bearing: Float
        if (direction?.isOK!!) {
            status = 1
            bearing = bearingBetweenLocations(destination!!, origin!!).toFloat()
            val route: Route = direction.routeList[0]!!
            googleMap?.addMarker(
                MarkerOptions().position(origin!!).icon(
                    bitmapDescriptorFromVector(
                        this@Home,
                        R.drawable.medium_topview
                    )
                ).anchor(0.5f, 0.5f).rotation(bearing).flat(true)
            )
            googleMap?.addMarker(MarkerOptions().position(destination!!))
            val directionPositionList: ArrayList<LatLng> = route.legList[0].directionPoint
            googleMap?.addPolyline(
                DirectionConverter.createPolyline(
                    this,
                    directionPositionList,
                    5,
                    Color.BLUE
                )
            )
            setCameraWithCoordinationBounds(route)

            AlertUtils.dismissDialog()

        } else {
            Toast.makeText(this@Home, direction.status, Toast.LENGTH_SHORT).show()
            AlertUtils.dismissDialog()
        }
    }

    private fun bearingBetweenLocations(latLng1: LatLng, latLng2: LatLng): Double {
        val PI = 3.14159
        val lat1 = latLng1.latitude * PI / 180
        val long1 = latLng1.longitude * PI / 180
        val lat2 = latLng2.latitude * PI / 180
        val long2 = latLng2.longitude * PI / 180
        val dLon = (long2 - long1)
        val y = Math.sin(dLon) * Math.cos(lat2)
        val x = (Math.cos(lat1) * Math.sin(lat2) - (Math.sin(lat1)
                * Math.cos(lat2) * Math.cos(dLon)))
        var brng = Math.atan2(y, x)
        brng = Math.toDegrees(brng)
        brng = (brng + 360) % 360
        return brng
    }

    override fun onDirectionFailure(t: Throwable?) {
        AlertUtils.dismissDialog()
    }

    private fun setCameraWithCoordinationBounds(route: Route) {
        val southwest: LatLng = route.bound.southwestCoordination.coordination
        val northeast: LatLng = route.bound.northeastCoordination.coordination
        val bounds: LatLngBounds = LatLngBounds(southwest, northeast)
        googleMap?.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100))
        try {
            if (location != null) {
                googleMap?.animateCamera(
                    CameraUpdateFactory.newLatLngZoom(
                        LatLng(
                            location!!.getLatitude(),
                            location!!.getLongitude()
                        ), 16.0f
                    )
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        AlertUtils.dismissDialog()
    }

// Google map diraction end


    private fun ShowAlertOrder() {
        val myApplicationInterface =
            AppController.instance?.requestQueue?.create(UserService::class.java)
        val call = myApplicationInterface?.getOrderAlertResponse(
            "application/json",
            OrderAlertRequest(UserShared.getUserID(this@Home)!!)
        )
        call?.enqueue(object : Callback<OrderAlertResponse> {
            override fun onFailure(call: Call<OrderAlertResponse>, t: Throwable) {

                Log.i("asdf", "findneworder=======" + t)

                Toast.makeText(this@Home, "Something Went Wrong!!", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(
                call: Call<OrderAlertResponse>,
                response: Response<OrderAlertResponse>
            ) {
                try {
                    if (response.body() != null) {
                        val orderAlertResponse = response.body()
                        Log.i("asdf", "orderdatais=======" + response.body())

                        if (orderAlertResponse?.status!!) {
                            if (orderAlertResponse.orders.isNotEmpty()) {

                                mainHandler.removeCallbacksAndMessages(null)

                                AlertLayout.visibility = View.VISIBLE
                                val ORDER = orderAlertResponse.orders[0]
                                ordersid = orderAlertResponse.orders[0].toString()
                                costumer_name.text = ORDER.userName
                                //address.text = ORDER.deliveryAddress
                                address.text = ORDER.deliveryAddress//33333333333333
                                var addresscust: String = ""

                                //getLocationFromAddress(ORDER.deliveryAddress)
                                getLocationFromAddress(ORDER.ordersaddress.address)
                                //timing.text= ActivityStartWaterDelivery.timeDistance.toString()


                                /*  if (orderAlertResponse.userphoto.photo!!.isNotEmpty()) {
                                  Glide.with(this@Home)
                                      .load(orderAlertResponse.userphoto.photo)
                                      .apply(RequestOptions.skipMemoryCacheOf(true))
                                      .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
                                      .into(profile_image);
                                  //  ImageLoader.getInstance().displayImage(UserShared.getUserPic(this@Home)!!, nav_view.getHeaderView(0).pic)
                              } else {
                                  profile_image.setImageResource(R.drawable.default_pic)
                              }*/

                                if (ORDER.ordersaddress.house_no == "null" || ORDER.ordersaddress.house_no == null) {
                                    addresscust = ORDER.ordersaddress.address
                                } else {
                                    addresscust =
                                        ORDER.ordersaddress.house_no + ORDER.ordersaddress.address
                                }

                                //address.text=ORDER.deliveryAddress
                                //timing.text=ActivityStartWaterDelivery.timeDistance.toString()

                                rating.text = String.format(
                                    "%.1f",
                                    java.lang.Double.parseDouble(orderAlertResponse.avg_rating)
                                )
                                OrderAcceptedOrNot(ORDER.id.toString())

                                // ordersid= orderAlertResponse.orders[0].toString()

                                customername = ORDER.userName
                                customerpaymentmode = ORDER.paymentMode
                                deliverytime = ActivityStartWaterDelivery.timeDistance.toString()
                                phonenumber = ORDER.userPhone

                                customeraddress = addresscust


                                OrderAccept.setOnClickListener {
                                    checkorderHandler.removeMessages(0)
                                    AlertLayout.visibility = View.GONE
                                    val orderID = ORDER.id.toString()

                                    mainHandler.removeCallbacksAndMessages(null)

                                    // ordersid=ORDER.id.toString()
                                    Log.i("asdf", "orderdatais=======" + orderID)
                                    OrderAlertConfirmation(1, orderID)
                                }
                                OrderReject.setOnClickListener {
                                    checkorderHandler.removeMessages(0)
                                    AlertLayout.visibility = View.GONE
                                    val orderID = ORDER.id.toString()

                                    OrderAlertConfirmation(0, orderID)
                                }
                            }

                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }

        })


    }

    private fun getLocationFromAddress(strAddress: String) {
        val coder = Geocoder(this)
        val address: List<Address>
        try {
            address = coder.getFromLocationName(strAddress, 5)
            if (address == null) {
            }
            val location = address.get(0)
            Log.i("sadf", "locationlat driver front=============" + latitude)
            Log.i("sadf", "locationlat driver front===========" + longitude);
            Log.i("sadf", "locationlat cust front===========" + location.getLatitude());
            Log.i("sadf", "locationlat cust front===========" + location.getLongitude());
            val Radius = 6371// radius of earth in K
            var lat1: Double? = LAT.toDouble()
            var lat2: Double = location.getLatitude()
            var lon1: Double? = LONG.toDouble()
            var lon2: Double = location.getLongitude()
            var dLat: Double = Math.toRadians(lat2 - lat1!!)
            Log.i("dsfds", "the dlat is====$dLat")
            val dLon = Math.toRadians(lon2 - lon1!!)
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


            var minute: Double = 4.0
            var time: Double
            time = minute * Radius * c
            Log.i("asdf================", (Radius * c.toDouble()).toString())
            Log.i("dfdsfds", "the data is on front=====$time")

            timing.text = (DecimalFormat("##.#")).format(time) + " min"
        } catch (e: ExceptionInInitializerError) {
            e.printStackTrace()
        }

        return
    }


    private fun ShowAlertOrderComplete() {

        orderReachStatus = sharedPref!!.getBoolean("orderReachLocationstatus", false)
        if (!orderReachStatus) {
            val mDialogView =
                LayoutInflater.from(this).inflate(R.layout.dailog_location_reach_conformation, null)
            val mBuilder = AlertDialog.Builder(this)
                .setView(mDialogView)


            val mAlertDialog = mBuilder.show()
            //login button click of custom layout
            mDialogView.YesReachLocation.setOnClickListener {


                // on order delivered

                val myApplicationInterface =
                    AppController.instance?.requestQueue?.create(UserService::class.java)
                val call = myApplicationInterface?.getDriverReachLocationResponse(
                    "application/json",
                    DriverReachLocationRequest(ordersid)
                )
                call?.enqueue(object : Callback<DriverReachLocationResponse> {
                    override fun onFailure(call: Call<DriverReachLocationResponse>, t: Throwable) {

                        Toast.makeText(this@Home, "Something Went Wrong!!", Toast.LENGTH_SHORT)
                            .show()

                    }

                    override fun onResponse(
                        call: Call<DriverReachLocationResponse>,
                        response: Response<DriverReachLocationResponse>
                    ) {
                        try {
                            if (response.body() != null) {
                                val orderDeliveredDriverResponse = response.body()
                                Log.i("asdf", "ordercomplete====first==" + response.body())
                                if (orderDeliveredDriverResponse?.status!!) {
                                    Toast.makeText(
                                        this@Home,
                                        orderDeliveredDriverResponse.message,
                                        Toast.LENGTH_SHORT
                                    ).show()

                                    Log.i("asdf", "ordercomplete===secound===" + response.body())


                                    sharedPref!!.edit()
                                        .putBoolean(
                                            "orderReachLocationstatus",
                                            orderDeliveredDriverResponse.status
                                        )
                                        .apply()

                                    startwaterdelivery.visibility = View.VISIBLE
                                    btnDelivery.visibility = View.GONE
                                    layNavigateCancel.visibility = View.GONE

                                    startwaterdelivery.setOnClickListener {
                                        val intent = Intent(
                                            applicationContext,
                                            ActivityStartWaterDelivery::class.java
                                        )
                                        startActivityForResult(intent, TASK_REQUEST)
                                    }

                                    /*  val intent = Intent(applicationContext, ActivityStartWaterDelivery::class.java)
                                  *//*intent.putExtra("orderid", ordersid)
                            intent.putExtra("customername", customername)
                            intent.putExtra("customeraddress",customeraddress )
                            intent.putExtra("paymentmode", customerpaymentmode)
                            intent.putExtra("Deliverytime", deliverytime)
                            intent.putExtra("phonenumber", phonenumber)*//*
                            startActivityForResult(intent, TASK_REQUEST)*/

                                    mAlertDialog.dismiss()

                                    //  setupTheMapForDriverLocation(LAT,LONG);

                                } else {
                                    Toast.makeText(
                                        this@Home,
                                        orderDeliveredDriverResponse.message,
                                        Toast.LENGTH_SHORT
                                    ).show()
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


    }


    private fun OrderAlertConfirmation(status: Int, orderID: String) {
        AlertUtils.showCustomProgressDialog(this@Home)
        val myApplicationInterface =
            AppController.instance?.requestQueue?.create(UserService::class.java)
        val call = myApplicationInterface?.getAcceptDriverResponse(
            "application/json",
            OrderAcceptDriverRequest(orderID, status, UserShared.getUserID(this@Home)!!)
        )
        call?.enqueue(object : Callback<OrderAcceptDriverResponse> {
            override fun onFailure(call: Call<OrderAcceptDriverResponse>, t: Throwable) {

                Toast.makeText(this@Home, "Something Went Wrong!!", Toast.LENGTH_SHORT).show()

            }

            override fun onResponse(
                call: Call<OrderAcceptDriverResponse>,
                response: Response<OrderAcceptDriverResponse>
            ) {
                if (response.body() != null) {
                    val orderAcceptDriverResponse = response.body()

                    Log.i("asfd", "orderalert======" + response.body())

                    if (orderAcceptDriverResponse?.status!!) {
                        Toast.makeText(
                            this@Home,
                            orderAcceptDriverResponse.message,
                            Toast.LENGTH_SHORT
                        )
                            .show()

                        mainHandler.removeCallbacksAndMessages(0)
                        // convertAddress(customeraddress)

                        /*val sharedPref: SharedPreferences = getSharedPreferences("myorderid", PRIVATE_MODE)
                    val editor = sharedPref.edit()
                    editor.putString("orderid", ordersid)
                    editor.putString("customeraddress", customeraddress)
                    editor.putString("customername", customername)
                    editor.putString("customerpaymentmode", customerpaymentmode)
                    editor.putString("deliverytime", deliverytime)
                    editor.putString("phonenumber", phonenumber)
                    editor.putString("customerlat", customerLat.toString())
                    editor.putString("customerlong", customerLong.toString())
                    editor.apply()*/



                        OrderDetail(orderID)


                    } else {
                        AlertUtils.dismissDialog()
                        Toast.makeText(
                            this@Home,
                            orderAcceptDriverResponse.message,
                            Toast.LENGTH_SHORT
                        )
                            .show()
                        CancelOrCompleteDelivery()
                        /*else {
                   // googleMap?.let { setMapLocation(it) }
                    Toast.makeText(this@Home, orderAcceptDriverResponse.message, Toast.LENGTH_SHORT).show()

                }*/
                    }
                }
            }
        })

    }

    private fun OrderDetail(orderID: String) {

        val myApplicationInterface =
            AppController.instance?.requestQueue?.create(UserService::class.java)
        val call = myApplicationInterface?.getOrderDetail(
            "application/json",
            OrderDetailRequest(orderID)
        )
        call?.enqueue(object : Callback<OrderDetailResponse> {
            override fun onFailure(call: Call<OrderDetailResponse>, t: Throwable) {

                Toast.makeText(this@Home, "Something Went Wrong!!", Toast.LENGTH_SHORT).show()

            }

            override fun onResponse(
                call: Call<OrderDetailResponse>,
                response: Response<OrderDetailResponse>
            ) {

                val orderDetailResponse = response.body()

                Log.i("asfd", "OrderDetails======" + response.body())


                if (orderDetailResponse != null) {
                    if (orderDetailResponse?.status!!) {
                        Toast.makeText(this@Home, orderDetailResponse.message, Toast.LENGTH_SHORT)
                            .show()

                        //convertAddress(customeraddress)


                        ordersid = ""
                        customeraddress = ""
                        customerLat = null
                        customerLong = null
                        customername = ""
                        customerpaymentmode = ""
                        deliverytime = ""
                        phonenumber = ""
                        customer_id = ""
                        delivery_address = ""


                        ordersid = orderDetailResponse.order_detailss.id
                        customeraddress = orderDetailResponse.order_detailss.user_address.address
                        customerLat = orderDetailResponse.order_detailss.user_address.lat.toDouble()
                        customerLong =
                            orderDetailResponse.order_detailss.user_address.lang.toDouble()
                        customername = orderDetailResponse.order_detailss.user_name
                        customerpaymentmode = orderDetailResponse.order_detailss.payment_mode
                        deliverytime = "15"
                        phonenumber = orderDetailResponse.order_detailss.user_phone
                        customer_id = orderDetailResponse.order_detailss.user_id
                        delivery_address = orderDetailResponse.order_detailss.delivery_address
                        house_number = orderDetailResponse.order_detailss.user_address.house_no
                        user_address_text = orderDetailResponse.order_detailss.user_address.address
                        val editor = sharedPref!!.edit()
                        editor.putString("orderid", ordersid)
                        editor.putString("customeraddress", customeraddress)
                        editor.putString("customername", customername)
                        editor.putString("customerpaymentmode", customerpaymentmode)
                        editor.putString("deliverytime", deliverytime)
                        editor.putString("phonenumber", phonenumber)
                        editor.putString("customerlat", customerLat.toString())
                        editor.putString("customerlong", customerLong.toString())
                        editor.putString("customerid", customer_id)
                        editor.putString("customerdelivery_address", delivery_address)
                        editor.putString(
                            "tankerType",
                            orderDetailResponse.order_detailss.tanker_type
                        )
                        editor.putString("amount", orderDetailResponse.order_detailss.amount)
                        editor.putString("discount", orderDetailResponse.order_detailss.discount)

                        editor.putString(
                            "house_no",
                            orderDetailResponse.order_detailss.user_address.house_no
                        )
                        editor.putString(
                            "customeraddtext",
                            orderDetailResponse.order_detailss.user_address.address
                        )
                        editor.putString("DriverLat", LAT)
                        editor.putString("DriverLong", LONG)
                        editor.apply()

                        DeliveryLocationAddress.text = user_address_text

                        Log.i("asd", "house_number==================" + house_number)


                        var houseno = ""
                        if (house_number === "null" || house_number == null) {


                            houseno = ""
                        } else {
                            houseno = house_number
                        }

                        DeliverToHouseNo.text = houseno

                        if (customerLat != null || customerLong != null) {
                            setupTheMapForDriverLocation()
                        } else {
                            convertAddress(customeraddress)
                        }

                        AlertUtils.dismissDialog()

                        doCancelCheck()


                    }
                }

                /*else {
                    // googleMap?.let { setMapLocation(it) }
                     Toast.makeText(this@Home, orderAcceptDriverResponse.message, Toast.LENGTH_SHORT).show()

                 }*/

            }
        })

    }

    private fun SetStatusOfTankerMan(status: Int) {

        AlertUtils.showCustomProgressDialog(this@Home)


        val myApplicationInterface =
            AppController.instance?.requestQueue?.create(UserService::class.java)
        val call = myApplicationInterface?.getTankerStatusResponse(
            "application/json",
            TankerStatusRequest(status, UserShared.getUserID(this@Home)!!)
        )
        call?.enqueue(object : Callback<TankerStatusResponse> {
            override fun onFailure(call: Call<TankerStatusResponse>, t: Throwable) {

                Toast.makeText(this@Home, "Something Went Wrong!", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(
                call: Call<TankerStatusResponse>,
                response: Response<TankerStatusResponse>
            ) {
                // AlertUtils.dismissDialog()
                val responseTank = response.body()
                Log.i("uhgdgfaua", responseTank.toString())

                if (responseTank?.status!!) {
                    Toast.makeText(this@Home, responseTank.message, Toast.LENGTH_SHORT).show()
                    /*  if (status == 1) {
                          StatusOfDriver.text = "Active"
                          StatusOfDriver.textColor = resources.getColor(R.color.colorAccent)
                      } else {
                          StatusOfDriver.text = "Inactive"
                          StatusOfDriver.textColor = resources.getColor(R.color.white)
                      }*/

                    checkIsDriverOnline()
                }
            }
        })
    }

    private fun GetCurrentLatLong() {
        if (ActivityCompat.checkSelfPermission(
                applicationContext,
                permissionsRequired[0]
            ) != PackageManager.PERMISSION_GRANTED
            || ActivityCompat.checkSelfPermission(
                applicationContext!!,
                permissionsRequired[1]
            ) != PackageManager.PERMISSION_GRANTED
            || ActivityCompat.checkSelfPermission(
                applicationContext!!,
                permissionsRequired[2]
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this@Home,
                    permissionsRequired[0]
                )
                || ActivityCompat.shouldShowRequestPermissionRationale(
                    this@Home,
                    permissionsRequired[1]
                )
                || ActivityCompat.shouldShowRequestPermissionRationale(
                    this@Home,
                    permissionsRequired[2]
                )
            ) {

                //Show Information about why you need the permission
                val builder = AlertDialog.Builder(this@Home)
                builder.setTitle("Need Multiple Permissions")
                builder.setMessage("This app needs Storage and Location permissions.")
                builder.setPositiveButton("Grant") { dialog, which ->
                    dialog.cancel()
                    ActivityCompat.requestPermissions(this@Home, permissionsRequired, 7)
                }
                builder.show()
            }
            ActivityCompat.requestPermissions(this@Home, permissionsRequired, 7)
        }
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (locationManager != null) {
            val isGPSEnabled = locationManager!!.isProviderEnabled("gps")

            if (isGPSEnabled) {
                locationManager!!.requestLocationUpdates(
                    GPS_PROVIDER,
                    10,
                    0f, object : LocationListener {
                        override fun onLocationChanged(location: Location) {
                            latitude = location.latitude
                            longitude = location.longitude
                            Log.i("sadf", "locationlat driver front=============" + latitude);
                            Log.i("sadf", "locationlat driver front=============" + longitude);
                            AppController.instance!!.driverlocationLatitude = latitude
                            AppController.instance!!.driverlocationLongitude = longitude
                        }

                        override fun onStatusChanged(s: String, i: Int, bundle: Bundle) {

                        }

                        override fun onProviderEnabled(s: String) {

                        }

                        override fun onProviderDisabled(s: String) {
                            val manager =
                                getSystemService(Context.LOCATION_SERVICE) as LocationManager
                            if (manager.isProviderEnabled(GPS_PROVIDER) && hasGPSDevice(this@Home)) {
                                Toast.makeText(this@Home, "Gps already enabled", Toast.LENGTH_SHORT)
                                    .show()

                            }

                            if (!hasGPSDevice(this@Home)) {
                                Toast.makeText(this@Home, "Gps not Supported", Toast.LENGTH_SHORT)
                                    .show()
                            }

                            if (!manager.isProviderEnabled(GPS_PROVIDER) && hasGPSDevice(this@Home)) {
                                android.util.Log.e("keshav", "Gps already enabled")
                                Toast.makeText(this@Home, "Gps not enabled", Toast.LENGTH_SHORT)
                                    .show()
                                enableLoc()
                            } else {
                                android.util.Log.e("keshav", "Gps already enabled")
                                Toast.makeText(this@Home, "Gps already enabled", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }
                    })

                android.util.Log.d("GPS Enabled", "GPS Enabled")
                if (locationManager != null) {
                    location = locationManager!!
                        .getLastKnownLocation("gps")
                } else {
                    location = locationManager!!
                        .getLastKnownLocation("gps")
                }

            } else {
                val manager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
                if (manager.isProviderEnabled(GPS_PROVIDER) && hasGPSDevice(this@Home)) {
                    Toast.makeText(this@Home, "Gps already enabled", Toast.LENGTH_SHORT).show()
                }

                if (!hasGPSDevice(this)) {
                    Toast.makeText(this@Home, "Gps not Supported", Toast.LENGTH_SHORT).show()
                }

                if (!manager.isProviderEnabled(GPS_PROVIDER) && hasGPSDevice(this@Home)) {
                    android.util.Log.e("keshav", "Gps already enabled")
                    Toast.makeText(this@Home, "Gps not enabled", Toast.LENGTH_SHORT).show()
                    enableLoc()
                } else {
                    android.util.Log.e("keshav", "GPS already enabled")
                    Toast.makeText(this@Home, "GPS already enabled", Toast.LENGTH_SHORT).show()
                }

                if (ActivityCompat.checkSelfPermission(
                        this@Home,
                        permissionsRequired[0]
                    ) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(
                        this@Home,
                        permissionsRequired[1]
                    ) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(
                        this@Home,
                        permissionsRequired[2]
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(
                            this@Home,
                            permissionsRequired[0]
                        )
                        || ActivityCompat.shouldShowRequestPermissionRationale(
                            this@Home,
                            permissionsRequired[1]
                        )
                        || ActivityCompat.shouldShowRequestPermissionRationale(
                            this@Home,
                            permissionsRequired[2]
                        )
                    ) {

                        //Show Information about why you need the permission
                        val builder = AlertDialog.Builder(this@Home)
                        builder.setTitle("Need Multiple Permissions")
                        builder.setMessage("This app needs Storage and Location permissions.")
                        builder.setPositiveButton("Grant") { dialog, which ->
                            dialog.cancel()
                            ActivityCompat.requestPermissions(this@Home, permissionsRequired, 7)
                        }
                        builder.show()
                    }
                    ActivityCompat.requestPermissions(this@Home, permissionsRequired, 7)
                }
            }
        }

        if (location != null) {
            lat = location!!.latitude
            longi = location!!.longitude
            LAT = lat.toString()
            LONG = longi.toString()
            GetAddressFromLatLang(LAT, LONG)

        }
        if (lat == null) {
            Toast.makeText(this@Home, "Searching Your Location", Toast.LENGTH_SHORT).show()
        } else {
            // Toast.makeText(getActivity(), String.valueOf(lat)+" Location", Toast.LENGTH_SHORT).show();
            // LAT = lat.toString()
            //LONG = longi.toString()
            GetAddressFromLatLang(LAT, LONG)

            Log.v("Location", LAT + " Long=Check" + LONG)

        }

    }

    fun convertAddress(address: String) {
        if (!address.isEmpty()) {
            try {
                val geocoder = Geocoder(this@Home, Locale.getDefault())
                val addressList: List<Address> = geocoder.getFromLocationName(address, 1)
                if (addressList != null && addressList.isNotEmpty()) {
                    val lat: Double = addressList[0].latitude
                    val lang: Double = addressList[0].longitude


                    customerLat = lat
                    customerLong = lang


                    setupTheMapForDriverLocation()

                    Log.v("Location", lat.toString() + " Long=" + lang.toString())
                }
            } catch (e: Exception) {
                Log.i("asdf", "exception======================" + e)
            } // end catch
        } // end if
    }

    private fun hasGPSDevice(context: Context): Boolean {
        val mgr = context
            .getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val providers = mgr.allProviders ?: return false
        return providers.contains(GPS_PROVIDER)
    }

    private fun enableLoc() {


        try {
            if (googleApiClient == null) {
                googleApiClient = GoogleApiClient.Builder(this@Home)
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(object : GoogleApiClient.ConnectionCallbacks {
                        override fun onConnected(bundle: Bundle?) {

                        }

                        override fun onConnectionSuspended(i: Int) {
                            googleApiClient?.connect()
                        }
                    })
                    .addOnConnectionFailedListener { connectionResult ->
                        android.util.Log.d(
                            "Location error",
                            "Location error " + connectionResult.errorCode
                        )
                    }.build()
                googleApiClient?.connect()

                val locationRequest = LocationRequest.create()
                locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
                locationRequest.interval = 30 * 1000
                locationRequest.fastestInterval = 5 * 1000
                val builder = LocationSettingsRequest.Builder()
                    .addLocationRequest(locationRequest)

                builder.setAlwaysShow(true)

                val result = LocationServices.SettingsApi.checkLocationSettings(
                    googleApiClient,
                    builder.build()
                )
                result.setResultCallback(ResultCallback<LocationSettingsResult> { result ->
                    val status = result.status
                    when (status.statusCode) {
                        LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> try {
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            status.startResolutionForResult(this@Home, REQUEST_LOCATION)
                        } catch (e: IntentSender.SendIntentException) {
                            // Ignore the error.
                        }
                    }
                })
            }
        } catch (e: WindowManager.BadTokenException) {
            Log.e("WindowManagerBad ", e.toString())
        }
    }


    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.home, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_settings -> return true
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {

            R.id.Order -> {
                val intent = Intent(applicationContext, BasicActivity::class.java)
                intent.putExtra("frag", "a")
                startActivity(intent)
            }
            R.id.Lang -> {
                val mDialogView = LayoutInflater.from(this).inflate(R.layout.change_language, null)

                if (langval == 1) {
                    mDialogView.rb_hindi.isChecked = true
                } else {
                    mDialogView.rb_english.isChecked = true
                }

                //AlertDialogBuilder
                val mBuilder = AlertDialog.Builder(this)
                    .setView(mDialogView)
                    .setCancelable(true)
                    .setTitle("Select Your Language")
                    .setPositiveButton("Ok", DialogInterface.OnClickListener { dialog, id ->
                        val rad = mDialogView.rg_lang.checkedRadioButtonId

                        if (rad == R.id.rb_hindi) {
                            val editor = sharedPrefLanguage!!.edit()
                            editor.putInt("lang", 1)
                            editor.apply()
                            dialog.dismiss()
                            reload()
                        } else if (rad == R.id.rb_english) {
                            val editor = sharedPrefLanguage!!.edit()
                            editor.putInt("lang", 0)
                            editor.apply()
                            dialog.dismiss()
                            reload()

                        }


                    })
                    .setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, id ->
                        dialog.cancel()
                    })
                val mAlertDialog = mBuilder.show()


            }
            R.id.Payouts -> {
                val intent = Intent(applicationContext, BasicActivity::class.java)
                intent.putExtra("frag", "b")
                startActivity(intent)
            }
            R.id.Logout -> {

                /*mainHandler.removeCallbacksAndMessages(null)
              handlerforLive.removeCallbacksAndMessages(null)
               handlerforOrder.removeCallbacksAndMessages(null)*/

                UserShared.removeUser(this)
                val intent = Intent(applicationContext, LogIn::class.java)
                startActivity(intent)
                finish()
            }

            R.id.History -> {
                val intent = Intent(applicationContext, Activity_History::class.java)
                startActivity(intent)
            }

            R.id.Help -> {
                val intent = Intent(applicationContext, BasicActivity::class.java)
                intent.putExtra("frag", "d")
                startActivity(intent)
            }
            R.id.Terms -> {
                WebViewType = "T"
                val intent = Intent(applicationContext, BasicActivity::class.java)
                intent.putExtra("frag", "f")
                startActivity(intent)
            }
            R.id.Privacy -> {
                WebViewType = "P"
                val intent = Intent(applicationContext, BasicActivity::class.java)
                intent.putExtra("frag", "f")
                startActivity(intent)
            }
            R.id.TripHistory -> {
                val intent = Intent(applicationContext, BasicActivity::class.java)
                intent.putExtra("frag", "h")
                startActivity(intent)
            }
            R.id.Ratings -> {
                val intent = Intent(applicationContext, BasicActivity::class.java)
                intent.putExtra("frag", "j")
                startActivity(intent)
            }
            R.id.wallet -> {
                val intent = Intent(applicationContext, BasicActivity::class.java)
                intent.putExtra("frag", "k")
                startActivity(intent)
            }

            /*  UserShared.removeUser(this@Home)
                      startActivity(Intent(this@Home, LogIn::class.java))
                      finishAffinity()*/

        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }


    fun reload() {
        val intent = Intent(applicationContext, Home::class.java)
        /* intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
         startActivity(intent)*/



        overridePendingTransition(0, 0)
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)

        overridePendingTransition(0, 0)
        startActivity(intent)
    }


    //Setting Up the Map
    private fun setMapLocation(map: GoogleMap) {


        with(map) {

            map.clear()

            val lat = LAT

            if (lat.isNotEmpty()) {
                moveCamera(
                    CameraUpdateFactory.newLatLngZoom(
                        LatLng(
                            LAT.toDouble(),
                            LONG.toDouble()
                        ), 15f
                    )
                )
                addMarker(
                    MarkerOptions().position(
                        LatLng(
                            LAT.toDouble(),
                            LONG.toDouble()
                        )
                    ).title("Your Location").icon(
                        bitmapDescriptorFromVector(this@Home, R.drawable.drop_location)
                    )
                )
                mapType = GoogleMap.MAP_TYPE_NORMAL

            }
        }
    }

    private fun bitmapDescriptorFromVector(context: Context, vectorResId: Int): BitmapDescriptor? {
        return ContextCompat.getDrawable(context, vectorResId)?.run {
            setBounds(0, 0, intrinsicWidth, intrinsicHeight)
            val bitmap =
                Bitmap.createBitmap(intrinsicWidth, intrinsicHeight, Bitmap.Config.ARGB_8888)
            draw(Canvas(bitmap))
            BitmapDescriptorFactory.fromBitmap(bitmap)
        }
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
        stopService(Intent(this, LocationService::class.java))
        stopService(Intent(this, FloatingViewService::class.java))
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
        handlerforLive.removeMessages(0)
        handlerforOrder.removeMessages(0)


    }

    override fun onStop() {
        super.onStop()

        handlerforLive.removeMessages(0)
        handlerforOrder.removeMessages(0)

    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }


    private fun CheckOrderAcceptedOrNot(orderID: String) {
        Log.i("asfd", "OrderAccepted===============" + orderID!!)
        val myApplicationInterface =
            AppController.instance?.requestQueue?.create(UserService::class.java)
        val call = myApplicationInterface?.getOrderDetail(
            "application/json",
            OrderDetailRequest(orderID!!)
        )
        call?.enqueue(object : Callback<OrderDetailResponse> {
            override fun onFailure(call: Call<OrderDetailResponse>, t: Throwable) {
                Log.i("asfd", "OrderAccepted===============" + t.message)

                Toast.makeText(this@Home, "Something Went Wrong!!", Toast.LENGTH_SHORT).show()

            }

            override fun onResponse(
                call: Call<OrderDetailResponse>,
                response: Response<OrderDetailResponse>
            ) {

                val orderDetailResponse = response.body()

                Log.i("asfd", "OrderAccepted===============" + response.body())

                if (orderDetailResponse != null) {
                    if (orderDetailResponse?.status!!) {

                        var ordstat: Int = orderDetailResponse?.order_detailss.status.toInt()

                        if (ordstat != 0) {
                            if (ActiveSwitch.isOn) {
                                SetStatusOfTankerMan(1)
                                layNavigateCancel.visibility = View.GONE
                                userAddress.visibility = View.GONE
                            } else {
                                SetStatusOfTankerMan(0)
                            }
                            GetCurrentLatLong()
                            checkorderHandler.removeMessages(0)
                            AlertLayout.visibility = View.GONE
                            costumer_name.text = ""
                            address.text = ""
                            rating.text = ""
                            customername = ""
                            customerpaymentmode = ""
                            deliverytime = ""
                            phonenumber = ""
                            customeraddress = ""
                        }
                    }
                }


            }
        })

    }

    /* private fun bitmapDescriptorFromVector(context: Context, vectorResId: Int): BitmapDescriptor {
         val vectorDrawable = ContextCompat.getDrawable(context, vectorResId)
         vectorDrawable!!.setBounds(0, 0, vectorDrawable.intrinsicWidth, vectorDrawable.intrinsicHeight)
         val bitmap =
             Bitmap.createBitmap(vectorDrawable.intrinsicWidth, vectorDrawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
         val canvas = Canvas(bitmap)
         vectorDrawable.draw(canvas)
         return BitmapDescriptorFactory.fromBitmap(bitmap)
     }*/
/*
    private fun NewShowLocation() {
        Places.initialize(applicationContext, resources?.getString(R.string.googme_map_key)!!)
        //   val placesClient = Places.createClient(this)
        val fields = Arrays.asList(Place.Field.ID, Place.Field.NAME)


        val intent = Autocomplete.IntentBuilder(
            AutocompleteActivityMode.OVERLAY, fields
        )
            .build(this)
        startActivityForResult(intent, PLACE_PICKER_REQUEST)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                val place = Autocomplete.getPlaceFromIntent(data!!)
                Log.v("Aniket", "Place: " + place.name + ", " + place.id)
                DeliveryLocation.text = Editable.Factory.getInstance().newEditable(place.name)
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                // TODO: Handle the error.
                val status = Autocomplete.getStatusFromIntent(data!!)
                Log.v("Aniket", status.statusMessage)
            } else if (resultCode == Activity.RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }

    }*/

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        try {
            Log.i("asdf", "this iscalled======" + resultCode)

            if (requestCode == 0) {


                Log.i("asdf", "this iscalled======")

                orderDeliveryStatus = sharedPref!!.getBoolean("orderStartDeliverystatus", false)

                if (orderDeliveryStatus) {
                    layNavigateCancel.visibility = View.GONE
                    userAddress.visibility = View.GONE
                    finishDelivery.visibility = View.VISIBLE
                    startwaterdelivery.visibility = View.GONE
                    finishDelivery.setOnClickListener {
                        val intent =
                            Intent(applicationContext, ActivityStartWaterDelivery::class.java)
                        startActivityForResult(intent, TASK_REQUEST)
                    }
                }

            } else if (requestCode == TASK_REQUEST) {
                var orderStatus = ""

                if (!data!!.getStringExtra("orderStatus").isNullOrEmpty()) {
                    orderStatus = data!!.getStringExtra("orderStatus")


                    Log.i("asdf", "orderstatus======" + orderStatus)

                    if (!orderStatus.equals(null) || !orderStatus.equals("")) {

                        if (orderStatus.equals("ordercancel")) {
                            handlerforCancel.removeMessages(0)
                            sharedPref!!.edit().clear().commit()


                            val intent = Intent(applicationContext, Home::class.java)
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                            startActivity(intent)

                        }

                        if (orderStatus.equals("orderfinesh")) {

                            //create order ratting
                            handlerforCancel.removeMessages(0)

                            val mDialogView =
                                LayoutInflater.from(this@Home)
                                    .inflate(R.layout.dailog_giveratingtotankerman, null)

                            val mBuilder = AlertDialog.Builder(this@Home)
                                .setView(mDialogView)

                            val AlertDialog = mBuilder.show()

                            AlertDialog.txttankermanname.text =
                                sharedPref!!.getString("customername", "")

                            //login button click of custom layout
                            AlertDialog.okcancel.setOnClickListener {

                                val msg = AlertDialog.ratrating.rating.toInt()

                                Log.i("asdf", "rating is=====" + msg)

                                Log.i(
                                    "asdf",
                                    "rating is=====" + Integer.toString(msg.toInt()).substring(
                                        0,
                                        1
                                    ).toInt()
                                )



                                GiveRating(msg, data!!.getStringExtra("orderid"))
                                AlertDialog.dismiss()

                                /*
                                sharedPref!!.edit().clear().commit()
                                val intent = Intent(applicationContext, Home::class.java)
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                                startActivity(intent)*/


                            }

                            AlertDialog.setOnDismissListener {
                                sharedPref!!.edit().clear().commit()
                                val intent = Intent(applicationContext, Home::class.java)
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                                startActivity(intent)
                            }


                        }

                        if (orderStatus.equals("orderDeliveryStart")) {

                            handlerforCancel.removeMessages(0)

                            orderDeliveryStatus =
                                sharedPref!!.getBoolean("orderStartDeliverystatus", false)
                            if (orderDeliveryStatus) {
                                layNavigateCancel.visibility = View.GONE
                                userAddress.visibility = View.GONE
                                startwaterdelivery.visibility = View.GONE
                                finishDelivery.visibility = View.VISIBLE
                                finishDelivery.setOnClickListener {
                                    val intent =
                                        Intent(
                                            applicationContext,
                                            ActivityStartWaterDelivery::class.java
                                        )
                                    startActivityForResult(intent, TASK_REQUEST)
                                }

                            }

                        }
                        if (orderStatus.equals("fineshBack")) {

                            return

                        }


                    }

                }
            } else if (requestCode == SYSTEM_ALERT_WINDOW_PERMISSION) {
                Log.i("asdf", "data=============" + data)
            } else if (requestCode == 9) {

                if (UserShared.getUserPic(this@Home)!!.isNotEmpty()) {
                    Glide.with(this@Home).load(UserShared.getUserPic(this@Home)!!)
                        .into(nav_view.getHeaderView(0).pic);
                    Glide.with(this@Home)
                        .load(UserShared.getUserPic(this@Home))
                        .apply(RequestOptions.skipMemoryCacheOf(true))
                        .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
                        .into(nav_view.getHeaderView(0).pic);
                    //  ImageLoader.getInstance().displayImage(UserShared.getUserPic(this@Home)!!, nav_view.getHeaderView(0).pic)
                } else {
                    nav_view.getHeaderView(0).pic.setImageResource(R.drawable.default_pic)
                }

            } else {
                Log.i("backed", "bcakedis==")

                /*
                            CancelOrCompleteDelivery()*/
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }


    private fun doCancelCheck() {

        handlerforCancel.postDelayed({

            val myApplicationInterface =
                AppController.instance?.requestQueue?.create(UserService::class.java)
            Log.i("dfdfdvvv", "the ordksflsfcske======" + ordersid)
            val call = myApplicationInterface?.getOrderDetail(
                "application/json",
                OrderDetailRequest(ordersid)

            )
            call?.enqueue(object : Callback<OrderDetailResponse> {
                override fun onFailure(call: Call<OrderDetailResponse>, t: Throwable) {

                    Toast.makeText(this@Home, "Something Went Wrong!!", Toast.LENGTH_SHORT).show()
                }

                override fun onResponse(
                    call: Call<OrderDetailResponse>,
                    response: Response<OrderDetailResponse>
                ) {
                    if (response.body() != null) {
                        val orderDetailResponse = response.body()

                        var can: Int = 0
                        can = orderDetailResponse!!.order_detailss.cancel_by

                        Log.i("asdf", "this is null =============" + can)


                        if (can == 0) {
                            doCancelCheck()
                        } else if (can == UserShared.getUserID(this@Home)!!.toInt()) {
                            handlerforCancel.removeMessages(0)
                            sharedPref!!.edit().clear().apply()


                            val intent = Intent(applicationContext, Home::class.java)
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                            startActivity(intent)


                            handlerforCancel.removeMessages(0)

                        } else {

                            handlerforCancel.removeMessages(0)
                            val mDialogView =
                                LayoutInflater.from(this@Home)
                                    .inflate(R.layout.dialog_cancel_by_customer, null)
                            val mBuilder = AlertDialog.Builder(this@Home)
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
            })


        }, 10000)


    }


    private fun GiveRating(rating: Int, orderids: String) {


        Log.i("asdf", "userratting=======" + sharedPref!!.getString("orderid", ""))

        AlertUtils.showCustomProgressDialog(this@Home)
        val myapplicationinterface =
            AppController.instance?.requestQueue?.create(UserService::class.java)
        val call = myapplicationinterface?.getRatingResponse(
            "application/json",
            RatingRequest(
                orderId = orderids,
                rating = rating,
                userId = UserShared.getUserID(this@Home)!!
            )
        )

        call?.enqueue(object : Callback<RatingResponse> {
            override fun onFailure(call: Call<RatingResponse>, t: Throwable) {
                AlertUtils.dismissDialog()
                Toast.makeText(this@Home, "Something is not Right", Toast.LENGTH_SHORT).show()

            }

            override fun onResponse(
                call: Call<RatingResponse>,
                response: Response<RatingResponse>
            ) {
                AlertUtils.dismissDialog()
            }
        })
    }

    private fun getDriverRatings() {

        val myApplicationInterface =
            AppController.instance?.requestQueue?.create(UserService::class.java)
        val call = myApplicationInterface?.getUserRating(
            "applicatiom/json",
            DriverRatingRequest(UserShared.getUserID(this@Home)!!)
        )
        call?.enqueue(object : Callback<DriverRatingResponse> {
            override fun onFailure(call: Call<DriverRatingResponse>, t: Throwable) {

                Log.i("asdf", "findneworder=======" + t)

                Toast.makeText(this@Home, "Something Went Wrong!!", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(
                call: Call<DriverRatingResponse>,
                response: Response<DriverRatingResponse>
            ) {
                val orderAlertResponse = response.body()
                Log.i("asdf", "ratingsis==============" + response.body())
                if (orderAlertResponse?.status!!) {
//                    val ratingOverall = findViewById<TextView>(R.id.ratingoverall) as TextView
                    try {
                        ratingoverall.text = orderAlertResponse.rating.toDouble().toString()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        })
    }


    override fun onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    override fun onConnectionSuspended(p0: Int) {

        Log.i("asf", "Connection Suspended");
        mGoogleApiClient.connect();
    }

    override fun onConnectionFailed(connectionResult: ConnectionResult) {
        Log.i("asdf", "Connection failed. Error: " + connectionResult.getErrorCode());
    }

    override fun onLocationChanged(location: Location) {
        if (location != null) {
            LAT = location.latitude.toString()
            LONG = location.longitude.toString()
            if (LAT.isNotEmpty()) {
                setMapLocation(googleMap!!)
                if (mGoogleApiClient != null) {
                    LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
                }
            } else {
                startLocationUpdates()
                GetCurrentLatLong()
                setMapLocation(googleMap!!)
                Toast.makeText(this@Home, "Can't Get Your Location", Toast.LENGTH_SHORT).show()
            }
            var msg =
                "Updated Location: Latitude " + location.longitude.toString() + location.longitude;
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        }
    }

    override fun onConnected(p0: Bundle?) {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            return;
        }


        startLocationUpdates();

        var fusedLocationProviderClient:
                FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        fusedLocationProviderClient.getLastLocation()
            .addOnSuccessListener(this, OnSuccessListener<Location> { location ->
                // Got last known location. In some rare situations this can be null.
                if (location != null) {
                    // Logic to handle location object
                    LAT = location.latitude.toString()
                    LONG = location.longitude.toString()

                    if (mGoogleApiClient != null) {
                        LocationServices.FusedLocationApi.removeLocationUpdates(
                            mGoogleApiClient,
                            this
                        );
                    }

                }
            })
    }


    private fun checkLocation(): Boolean {
        if (!isLocationEnabled())
            showAlert();
        return isLocationEnabled();
    }

    private fun isLocationEnabled(): Boolean {
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager!!.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    private fun showAlert() {
        val dialog = AlertDialog.Builder(this)
        dialog.setTitle("Enable Location")
            .setMessage("Your Locations Settings is set to 'Off'.\nPlease Enable Location to " + "use this app")
            .setPositiveButton(
                "Location Settings",
                DialogInterface.OnClickListener { paramDialogInterface, paramInt ->
                    val myIntent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                    startActivity(myIntent)
                })
            .setNegativeButton(
                "Cancel",
                DialogInterface.OnClickListener { paramDialogInterface, paramInt -> })
        dialog.show()
    }

    protected fun startLocationUpdates() {

        // Create the location request
        mLocationRequest = LocationRequest.create()
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
            .setInterval(UPDATE_INTERVAL)
            .setFastestInterval(FASTEST_INTERVAL);
        // Request location updates
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(
            mGoogleApiClient,
            mLocationRequest, this
        );
    }


}