package com.example.letspaanidriver.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import android.view.animation.AnimationUtils
import com.example.letspaanidriver.R
import com.example.letspaanidriver.utils.LocationService
import com.example.letspani.utils.UserShared
import kotlinx.android.synthetic.main.splash.*

class Splash : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash)


        //For Full Screen of Activity
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)

        val animation = AnimationUtils.loadAnimation(this@Splash, R.anim.zoom_out)
        logo.startAnimation(animation)

        val fromBottom = AnimationUtils.loadAnimation(this@Splash, R.anim.bottom_up)
        pani_text.startAnimation(fromBottom)

//Run method for Splash Screen
        Handler().postDelayed({


            if (UserShared.getUserID(this@Splash)?.isNotEmpty()!!){
                val intent = Intent(this@Splash, Home::class.java)
                startActivity(intent)
                finish()
            }
            else{
                val intent = Intent(this@Splash, LogIn::class.java)
                startActivity(intent)
                finish()
            }
/*


            val intent = Intent(this@Splash, LogIn::class.java)
            startActivity(intent)
            finish()*/
            //   overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        }, 1000)
    }
}
