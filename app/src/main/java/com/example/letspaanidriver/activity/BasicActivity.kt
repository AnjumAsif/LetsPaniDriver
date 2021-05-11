package com.example.letspaanidriver.activity

import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.letspaanidriver.R
import com.example.letspaanidriver.fragments.*
import com.example.letspani.fragments.*
import java.util.*

class BasicActivity : AppCompatActivity() {


    private var PRIVATE_MODE = 0
    var sharedPrefLanguage: SharedPreferences? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_basic)

        sharedPrefLanguage = getSharedPreferences("language", PRIVATE_MODE)

        var langval =  sharedPrefLanguage!!.getInt("lang",0)

        if(langval==1)
        {
            val locale = Locale("hi")
            Locale.setDefault(locale)
            val config = Configuration()
            config.locale = locale
            baseContext.resources.updateConfiguration(
                config,
                baseContext.resources.displayMetrics
            )
            setContentView(R.layout.activity_basic)

        }
        else{
            setContentView(R.layout.activity_basic)
        }


        val frag = intent.getStringExtra("frag")

        when (frag) {
            "a" -> {
                supportFragmentManager.beginTransaction().replace(R.id.FrameLayout, OrderPreferences()).commit()
            }
            "b" -> {
                supportFragmentManager.beginTransaction().replace(R.id.FrameLayout, Payouts()).commit()
            }
            "c" -> {
                supportFragmentManager.beginTransaction().replace(R.id.FrameLayout, Profile()).commit()
            }
            "d" -> {
                supportFragmentManager.beginTransaction().replace(R.id.FrameLayout, Support()).commit()
            }
            "f" -> {
                supportFragmentManager.beginTransaction().replace(R.id.FrameLayout, Webview()).commit()
            }
            "h" -> {
                supportFragmentManager.beginTransaction().replace(R.id.FrameLayout, YourOrder()).commit()
            }
            "g" -> {
                supportFragmentManager.beginTransaction().replace(R.id.FrameLayout, OrderDetails()).commit()
            }
            "i" -> {
                supportFragmentManager.beginTransaction().replace(R.id.FrameLayout, UploadDocs()).commit()
            }
            "j" -> {
                supportFragmentManager.beginTransaction().replace(R.id.FrameLayout, Ratings()).commit()
            }
            "k" -> {
                supportFragmentManager.beginTransaction().replace(R.id.FrameLayout, Wallet()).commit()
            }


        }

    }
}
