package com.example.letspaanidriver.utils


import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.Intent.getIntent
import android.content.SharedPreferences
import android.graphics.PixelFormat
import android.os.IBinder
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import com.example.letspaanidriver.R
import com.example.letspaanidriver.activity.Home
import org.jetbrains.anko.sdk25.coroutines.onTouch


class FloatingViewService : Service(), View.OnClickListener {
    private var mWindowManager: WindowManager? = null
    private var mFloatingView: View? = null
    private var PRIVATE_MODE = 0
    var orderid: String? = ""
    var customeraddress: String? = ""
    var customername: String? = ""
    var customerpaymentmode: String? = ""
    var deliverytime: String? = ""
    var phonenumber: String? = ""
    var custLat: String? = ""
    var custLong: String? = ""


    override fun onBind(intent: Intent): IBinder? {

        return null
    }

    override fun onCreate() {
        super.onCreate()

        mFloatingView = LayoutInflater.from(this).inflate(R.layout.layout_floating_widget, null)
        val params: WindowManager.LayoutParams

        val sharedPref: SharedPreferences = getSharedPreferences("myorderid", PRIVATE_MODE)
        Log.i("asdf", "orderidis====" + sharedPref.getString("orderid", ""))
        orderid = sharedPref.getString("orderid", "")

        customeraddress = sharedPref.getString("customeraddress", "")
        customername = sharedPref.getString("customername", "")
        customerpaymentmode = sharedPref.getString("customerpaymentmode", "")
        deliverytime = sharedPref.getString("deliverytime", "")
        phonenumber = sharedPref.getString("phonenumber", "")

        custLat = sharedPref.getString("customerlat", "")
        custLong = sharedPref.getString("customerlong", "")


        sharedPref.edit().clear().commit()


        // mFloatingView.gravity = Gravity.CENTER_HORIZONTAL or Gravity.BOTTOM

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            params = WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,

                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT
            )
        } else {
            params = WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT
            )
        }
        params.gravity = Gravity.CENTER_VERTICAL or Gravity.LEFT
        mWindowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
        mWindowManager!!.addView(mFloatingView, params)
        mFloatingView!!.findViewById<View>(R.id.buttonClose).setOnClickListener(this)


    }

    @Override
    fun onHandleIntent() {


    }

    override fun onDestroy() {
        super.onDestroy()
        if (mFloatingView != null) mWindowManager!!.removeView(mFloatingView)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.buttonClose -> {

                val launchIntent =
                    packageManager.getLaunchIntentForPackage("com.example.letspaanidriver")
                if (launchIntent != null) {
                    launchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(launchIntent)
                }
                /*   Log.i("asdf","serviceorderid======="+orderid);
                   var orderid : String?=""
                   var driveraddress : String?=""
                   var customername : String?=""
                   var customerpaymentmode : String?=""
                   var deliverytime : String?=""
                   var phonenumber : String?=""*/

//
//                val intent = Intent(applicationContext, Home::class.java)
//                intent.putExtra("orderid",orderid)
//
//                intent.putExtra("customeraddress",customeraddress)
//                intent.putExtra("customername",customername)
//                intent.putExtra("customerpaymentmode",customerpaymentmode)
//                intent.putExtra("deliverytime",deliverytime)
//                intent.putExtra("phonenumber",phonenumber)
//
//                intent.putExtra("customerlat",custLat)
//                intent.putExtra("customerlong",custLong)
//
//
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//                startActivity(intent)
//
//                stopSelf()
            }
        }
    }
}
