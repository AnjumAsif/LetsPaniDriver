package com.example.letspaanidriver.utils

import android.Manifest
import android.annotation.SuppressLint
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.appcompat.app.AlertDialog
import android.util.Log
import android.widget.Toast
import com.example.letspaanidriver.activity.Home
import com.example.letspaanidriver.apis.classes.LiveLocationRequest
import com.example.letspaanidriver.apis.classes.LiveLocationResponse
import com.example.letspani.apis.AppController
import com.example.letspani.apis.UserService
import com.example.letspani.utils.UserShared
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LocationService : Service() {

    private var mLocationManager: LocationManager? = null
    var handler = Handler()
    var delayMiles: Long = 10000
    var iconut = 0
    var mLocationListeners = arrayOf(
        LocationListener(LocationManager.GPS_PROVIDER),
        LocationListener(LocationManager.NETWORK_PROVIDER)
    )
    private var mLocationRequest: LocationRequest? = null

    class LocationListener(provider: String) : android.location.LocationListener {
        internal var mLastLocation: Location

        init {
            Log.e(TAG, "serviceLocationListener $provider")
            mLastLocation = Location(provider)
        }

        override fun onLocationChanged(location: Location) {
            Log.e(TAG, "serviceonLocationChanged: $location")
            mLastLocation.set(location)


            AppController.instance!!.driverlocationLatitude = mLastLocation.latitude
            AppController.instance!!.driverlocationLongitude = mLastLocation.longitude

            Log.v(
                "asdf",
                "serviceLastLocationbackground" + mLastLocation.latitude.toString() + "  " + mLastLocation.longitude.toString()
            )
        }

        override fun onProviderDisabled(provider: String) {
            Log.e(TAG, "serviceonProviderDisabled: $provider")
        }

        override fun onProviderEnabled(provider: String) {
            Log.e(TAG, "serviceonProviderEnabled: $provider")
        }

        override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {
            Log.e(TAG, "serviceonStatusChanged: $provider")
        }
    }

    override fun onBind(arg0: Intent): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.e(TAG, "serviceonStartCommand")
        super.onStartCommand(intent, flags, startId)
        //  return Service.START_STICKY

        handler.postDelayed(object : Runnable {
            @RequiresApi(api = Build.VERSION_CODES.P)
            public override fun run() {
                handler.postDelayed(this, delayMiles)
                initializeLocationManager()
                try {
                    mLocationManager!!.requestLocationUpdates(
                        LocationManager.NETWORK_PROVIDER,
                        LOCATION_INTERVAL.toLong(),
                        LOCATION_DISTANCE,
                        mLocationListeners[1]
                    )
                } catch (ex: java.lang.SecurityException) {
                    Log.i(TAG, "fail to request location update, ignore", ex)
                } catch (ex: IllegalArgumentException) {
                    Log.d(TAG, "network provider does not exist, " + ex.message)
                }

                try {
                    mLocationManager!!.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER, LOCATION_INTERVAL.toLong(), LOCATION_DISTANCE,
                        mLocationListeners[0]
                    )
                } catch (ex: java.lang.SecurityException) {
                    Log.i(TAG, "fail to request location update, ignore", ex)
                } catch (ex: IllegalArgumentException) {
                    Log.d(TAG, "gps provider does not exist " + ex.message)
                }

                //  Home.backgroundSenduserLocation()

                Log.i(
                    "tag",
                    "backgroundlatitude========" + AppController.instance!!.driverlocationLatitude.toString()
                )
                Log.i(
                    "tag",
                    "backgroundlongitude========" + AppController.instance!!.driverlocationLongitude.toString()
                )


                val myApplicationInterface =
                    AppController.instance?.requestQueue?.create(UserService::class.java)
                val call = myApplicationInterface?.getDriverLiveResponse(
                    "application/json",
                    LiveLocationRequest(
                        AppController.instance!!.driverlocationLongitude.toString(),
                        AppController.instance!!.driverlocationLatitude.toString(),

                        "asdfd",
                        UserShared.getUserID(applicationContext)!!
                    )
                )
                call?.enqueue(object : Callback<LiveLocationResponse> {
                    override fun onFailure(call: Call<LiveLocationResponse>, t: Throwable) {
                        Toast.makeText(
                            applicationContext,
                            "Something Went Wrong!!" + t.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    override fun onResponse(
                        call: Call<LiveLocationResponse>,
                        response: Response<LiveLocationResponse>
                    ) {
                        val liveLocationResponse = response.body()

                        Log.i("asdf", "response====")

                        if (liveLocationResponse != null) {
                            if (liveLocationResponse?.status!!) {
                                Toast.makeText(
                                    applicationContext,
                                    liveLocationResponse.message + "background",
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
                                    applicationContext,
                                    liveLocationResponse.message,
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                            }
                        }

                    }
                })


            }
        }, delayMiles) //Run in every 10 seconds
        return START_STICKY
    }

    override fun onCreate() {
        Log.e(TAG, "serviceonCreate")


    }

    override fun onDestroy() {
        Log.e(TAG, "serviceonDestroy")
        super.onDestroy()
        if (mLocationManager != null) {
            for (i in mLocationListeners.indices) {
                try {
                    mLocationManager!!.removeUpdates(mLocationListeners[i])
                } catch (ex: Exception) {
                    Log.i(TAG, "servicefail to remove location listners, ignore", ex)
                }

            }
        }
    }

    private fun initializeLocationManager() {
        Log.e(TAG, "initializeLocationManager")
        if (mLocationManager == null) {
            mLocationManager =
                applicationContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        }
    }

    companion object {
        private val TAG = "BOOMBOOMTESTGPS"
        private val LOCATION_INTERVAL = 10000
        private val LOCATION_DISTANCE = 0f
    }


}