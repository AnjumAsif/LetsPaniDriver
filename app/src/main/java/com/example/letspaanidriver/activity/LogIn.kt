package com.example.letspaanidriver.activity

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.AdapterView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.letspaanidriver.R
import com.example.letspani.apis.AppController
import com.example.letspani.apis.UserService
import com.example.letspani.apis.classes.LoginOtpRequest
import com.example.letspani.apis.classes.LoginOtpResponse
import com.example.letspani.utils.AlertUtils
import com.example.letspani.utils.UserShared
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.iid.FirebaseInstanceId
import kotlinx.android.synthetic.main.activity_log_in.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class LogIn : AppCompatActivity() {

    private var PRIVATE_MODE = 0
    var sharedPrefLanguage: SharedPreferences? = null


    internal var permissionsRequired = arrayOf(
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.CALL_PHONE,
        Manifest.permission.INTERNET
    )


    private val SYSTEM_ALERT_WINDOW_PERMISSION = 2084

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
            setContentView(R.layout.activity_log_in)
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
            setContentView(R.layout.activity_log_in)
        } else {
            setContentView(R.layout.activity_log_in)
        }


        val bounce = AnimationUtils.loadAnimation(this@LogIn, R.anim.bounce)

        InnerLogo.startAnimation(bounce)
        GetCurrentLatLong()
        RegisterNow.setOnClickListener { startActivity(Intent(this@LogIn, Register::class.java)) }

        //  rotateloading.start()

        Login.setOnClickListener {

            val permission1 = android.Manifest.permission.ACCESS_COARSE_LOCATION
            val permission2 = android.Manifest.permission.ACCESS_FINE_LOCATION

            val checkper1 = checkWriteExternalPermission(permission1)
            val checkper2 = checkWriteExternalPermission(permission2)

            if (checkper1 && checkper2) {

                val number = MobileNumber.text.toString()
                if (number == "") {
                    Snackbar.make(it, "Please Enter Mobile Number", Snackbar.LENGTH_SHORT).show()
                } else {

                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                        VerifyNumber(number)
                    } else if (Settings.canDrawOverlays(this)) {
                        VerifyNumber(number)
                    } else {
                        askPermission()
                        Toast.makeText(
                            this,
                            "You need System Alert Window Permission to do this",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

            } else {
                GetCurrentLatLong()
            }


        }

        chkLang.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                // Display the selected item text on text view
                Log.i("SDfsdf", "thjfsdnflk" + chkLang.selectedItem.toString())

                if (chkLang.selectedItemPosition == 2) {
                    val editor = sharedPrefLanguage!!.edit()
                    editor.putInt("lang", 0)
                    editor.apply()
                    reload()
                }

                if (chkLang.selectedItemPosition == 1) {
                    val editor = sharedPrefLanguage!!.edit()
                    editor.putInt("lang", 1)
                    editor.apply()
                    reload()
                }

            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Another interface callback
            }
        }


        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.i("asdf", "getInstanceId failed", task.exception)
                    return@OnCompleteListener
                }

                // Get new Instance ID token
                val token = task.result?.token

                UserShared.setFirebaseToken(this@LogIn, token!!)


            })

        val permission1 = android.Manifest.permission.ACCESS_COARSE_LOCATION
        val permission2 = android.Manifest.permission.ACCESS_FINE_LOCATION

        val checkper1 = checkWriteExternalPermission(permission1)
        val checkper2 = checkWriteExternalPermission(permission2)

        if (checkper1 && checkper2) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {

            } else if (Settings.canDrawOverlays(this)) {

            } else {
                askPermission()
                Toast.makeText(
                    this,
                    "You need System Alert Window Permission to do this",
                    Toast.LENGTH_SHORT
                ).show()
            }
        } else {

            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {

            } else if (Settings.canDrawOverlays(this)) {

            } else {
                askPermission()
                Toast.makeText(
                    this,
                    "You need System Alert Window Permission to do this",
                    Toast.LENGTH_SHORT
                ).show()
            }

            GetCurrentLatLong()
        }
        if( isXiaomi()) {

            goToXiaomiPermissions(this)
        }
/*
        chkLang.setOnCheckedChangeListener { buttonView, isChecked ->

            if (isChecked) {
                val editor = sharedPrefLanguage!!.edit()
                editor.putInt("lang", 1)
                editor.apply()
                reload()
              chkLang.isChecked
            }
            else {
                val editor = sharedPrefLanguage!!.edit()
                editor.putInt("lang", 0)
                editor.apply()
                reload()
            }
        }

*/

    }

    /*  override fun onCreateOptionsMenu(menu:Menu):Boolean {
          val menuInflater = getMenuInflater()
          menuInflater.inflate(R.menu.languageselector, menu)
          return true
      }

      override fun onOptionsItemSelected(item: MenuItem): Boolean {
          return when (item.itemId) {
              R.id.menuhindi -> {
                  Toast.makeText(applicationContext, "click on hindi", Toast.LENGTH_LONG).show()
                  true
              }
              R.id.menuenglish ->{
                  Toast.makeText(applicationContext, "click on english", Toast.LENGTH_LONG).show()
                  return true
              }

              else -> super.onOptionsItemSelected(item)
          }
      }
  */
    private fun checkWriteExternalPermission(permission: String): Boolean {

        val res = applicationContext.checkCallingOrSelfPermission(permission)
        return res == PackageManager.PERMISSION_GRANTED
    }

    private fun askPermission() {
        val intent = Intent(
            Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
            Uri.parse("package:$packageName")
        )
        startActivityForResult(intent, SYSTEM_ALERT_WINDOW_PERMISSION)
    }
    fun isXiaomi():Boolean {
        return "xiaomi".equals(Build.MANUFACTURER, ignoreCase = true)
    }

    fun goToXiaomiPermissions(context: Context) {

        try {
            val intent = Intent("miui.intent.action.APP_PERM_EDITOR")
            intent.setClassName(
                "com.miui.securitycenter",
                "com.miui.permcenter.permissions.PermissionsEditorActivity"
            )
            intent.putExtra("extra_pkgname", context.packageName)
            context.startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        /* val intent = Intent("miui.intent.action.APP_PERM_EDITOR")
         intent.setClassName("com.miui.securitycenter", "com.miui.permcenter.permissions.PermissionsEditorActivity")
         intent.putExtra("extra_pkgname", context.packageName)
         context.startActivity(intent)*/
    }
    private fun VerifyNumber(number: String) {
        AlertUtils.showCustomProgressDialog(this@LogIn)
        Log.i("asdf", "tokenis=============" + UserShared.getFirebaseToken(this@LogIn)!!)
        //Loader of Data
        val myApplicationInterface =
            AppController?.instance?.requestQueue?.create(UserService::class.java)
        val call = myApplicationInterface?.getLoginOtpResponse(
            "application/json", LoginOtpRequest(
                number, "driver",
                UserShared.getFirebaseToken(this@LogIn)!!
            )
        )
        call?.enqueue(object : Callback<LoginOtpResponse> {
            override fun onFailure(call: Call<LoginOtpResponse>, t: Throwable) {
                AlertUtils.dismissDialog()
                Toast.makeText(applicationContext, "Some Error Occurred", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(
                call: Call<LoginOtpResponse>,
                response: Response<LoginOtpResponse>
            ) {
                AlertUtils.dismissDialog()
                val loginOTPresponse = response.body()

                Log.i("asdf", "logindriver=======" + response.body())

                if (loginOTPresponse!!.status) {

                    // Toast.makeText(applicationContext, "Your OTP ${loginOTPresponse.otp}", Toast.LENGTH_LONG).show()
                    val intent = Intent(this@LogIn, OTP::class.java)
                    intent.putExtra("Number", number)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(applicationContext, loginOTPresponse.message, Toast.LENGTH_SHORT)
                        .show()
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
        /*|| ActivityCompat.checkSelfPermission(
            applicationContext!!,
            permissionsRequired[3]
        ) != PackageManager.PERMISSION_GRANTED*/
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this@LogIn,
                    permissionsRequired[0]
                )
                || ActivityCompat.shouldShowRequestPermissionRationale(
                    this@LogIn,
                    permissionsRequired[1]
                )
                || ActivityCompat.shouldShowRequestPermissionRationale(
                    this@LogIn,
                    permissionsRequired[2]
                )
            /*|| ActivityCompat.shouldShowRequestPermissionRationale(this@LogIn, permissionsRequired[3])*/
            ) {

                //Show Information about why you need the permission
                val builder = AlertDialog.Builder(this@LogIn)
                builder.setTitle("Need Multiple Permissions")
                builder.setMessage("This app needs Storage and Location permissions.")
                builder.setPositiveButton("Grant") { dialog, which ->
                    dialog.cancel()
                    ActivityCompat.requestPermissions(this@LogIn, permissionsRequired, 7)
                }


                builder.show()
            }

            ActivityCompat.requestPermissions(this@LogIn, permissionsRequired, 7)


        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == SYSTEM_ALERT_WINDOW_PERMISSION) {
            Log.i("asdf", "data=============" + data)
        }
    }

    fun reload() {
        val intent = intent
        overridePendingTransition(0, 0)
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
        finish()
        overridePendingTransition(0, 0)
        startActivity(intent)
    }
}
