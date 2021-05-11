package com.example.letspaanidriver.activity

import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast


import com.example.letspaanidriver.R
import com.example.letspani.apis.AppController
import com.example.letspani.apis.UserService
import com.example.letspani.apis.classes.OTPRequest
import com.example.letspani.apis.classes.OTPResponse
import com.example.letspani.utils.AlertUtils
import com.example.letspani.utils.UserShared
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import kotlinx.android.synthetic.main.otp.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class OTP : AppCompatActivity() {

    var number: String = ""
    private var PRIVATE_MODE = 0
    var sharedPrefLanguage: SharedPreferences? = null


    companion object {
        var UserID: Int? = null
    }

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
            setContentView(R.layout.otp)
        } else {
            setContentView(R.layout.otp)
        }


        number = intent.getStringExtra("Number")
        numberOTP.text = number
        otp_code1.requestFocus()


        otp_code1.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
            }

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

            }

            override fun afterTextChanged(editable: Editable) {
                if (otp_code1.text.toString().length == 1) {
                    otp_code2.requestFocus()
                }
            }
        })

        otp_code2.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

            }

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                if (otp_code2.text.toString().isEmpty()) {
                    otp_code1.requestFocus()
                }
            }

            override fun afterTextChanged(editable: Editable) {
                if (otp_code2.text.toString().length == 1) {
                    otp_code3.requestFocus()
                }
            }
        })

        otp_code3.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

            }

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                if (otp_code3.text.toString().isEmpty()) {
                    otp_code2.requestFocus()
                }
            }

            override fun afterTextChanged(editable: Editable) {
                if (otp_code3.text.toString().length == 1) {
                    otp_code4.requestFocus()
                }
            }
        })

        otp_code4.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

            }

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                if (otp_code4.text.toString().isEmpty()) {
                    otp_code3.requestFocus()
                }
            }

            override fun afterTextChanged(editable: Editable) {
                // We can call_black api to verify the OTP here or on an explicit button click
                if (otp_code4.text.toString().length == 1) {
                    otp_code5.requestFocus()
                }

            }
        })

        otp_code5.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

            }

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                if (otp_code5.text.toString().isEmpty()) {
                    otp_code4.requestFocus()
                }
            }

            override fun afterTextChanged(editable: Editable) {
                // We can call_black api to verify the OTP here or on an explicit button click
                if (otp_code5.text.toString().length == 1) {
                    otp_code6.requestFocus()
                }
            }
        })


        otp_code6.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

            }

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                if (otp_code6.text.toString().isEmpty()) {
                    otp_code5.requestFocus()
                }
            }

            override fun afterTextChanged(editable: Editable) {
                // We can call_black api to verify the OTP here or on an explicit button click
                OTPVerification()
            }
        })
    }

    private fun OTPVerification() {

        val otp = otp_code1.text.toString() + otp_code2.text.toString() +
                otp_code3.text.toString() +
                otp_code4.text.toString() +
                otp_code5.text.toString() +
                otp_code6.text.toString()

        AlertUtils.showCustomProgressDialog(this@OTP)

        val myApplicationInterface =
            AppController.instance?.requestQueue?.create(UserService::class.java)
        val call =
            myApplicationInterface?.getOTPResponse(
                "application/json",
                OTPRequest(contactNumber = number, otp = otp)
            )
        call?.enqueue(object : Callback<OTPResponse> {
            override fun onFailure(call: Call<OTPResponse>, t: Throwable) {
                AlertUtils.dismissDialog()
                Toast.makeText(applicationContext, "Something Went Wrong!!", Toast.LENGTH_SHORT)
                    .show()
            }

            override fun onResponse(call: Call<OTPResponse>, response: Response<OTPResponse>) {
                AlertUtils.dismissDialog()
                val otpResponse = response.body()

                Log.i("regis", "registerdetails====" + response.body());

                if (otpResponse!!.status) {

                    FirebaseInstanceId.getInstance().instanceId
                        .addOnCompleteListener(OnCompleteListener { task ->
                            if (!task.isSuccessful) {
                                Log.i("asdf", "getInstanceId failed", task.exception)
                                return@OnCompleteListener
                            }
                            // Get new Instance ID token
                            val token = task.result?.token

                            UserShared.setFirebaseToken(this@OTP, token!!)

                            Log.i("asdf", "tokenis=============" + token)
                        })

                    UserID = otpResponse.user.id

                    UserShared.setUserID(this@OTP, otpResponse.user.id.toString())
                    UserShared.setUserName(this@OTP, otpResponse.user.name)

                    if (otpResponse.user.photo != null) {
                        UserShared.setUserPic(this@OTP, otpResponse.user.photo)
                    }
                    Toast.makeText(applicationContext, otpResponse.message, Toast.LENGTH_SHORT)
                        .show()
                    startActivity(Intent(this@OTP, Home::class.java))
                    finish()

                } else {
                    Toast.makeText(applicationContext, otpResponse.message, Toast.LENGTH_SHORT)
                        .show()
                }
            }
        })
    }
}
