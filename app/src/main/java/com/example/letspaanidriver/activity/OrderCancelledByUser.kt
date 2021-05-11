package com.example.letspaanidriver.activity

import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast
import com.example.letspaanidriver.R
import com.example.letspaanidriver.utils.FirebaseRecived
import com.example.letspani.apis.AppController
import com.example.letspani.apis.UserService
import com.example.letspani.apis.classes.RatingRequest
import com.example.letspani.apis.classes.RatingResponse
import com.example.letspani.utils.AlertUtils
import com.example.letspani.utils.UserShared
import kotlinx.android.synthetic.main.rating.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class OrderCancelledByUser : AppCompatActivity() {
    var rating: Int = 0
    var sharedPref: SharedPreferences? = null
    private var PRIVATE_MODE = 0
    var sharedPrefLanguage: SharedPreferences? = null
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
            setContentView(R.layout.activity_ordercancelbyuser)
        } else {
            setContentView(R.layout.activity_ordercancelbyuser)
        }





        sharedPref = getSharedPreferences("OrderDetail", PRIVATE_MODE)
        sharedPref!!.edit().clear().commit()
        Submit.setOnClickListener {
            val intent = Intent(applicationContext, Home::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
        }

        back.setOnClickListener {
            val intent = Intent(applicationContext, Home::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(applicationContext, Home::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }


}
