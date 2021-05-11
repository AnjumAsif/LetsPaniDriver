package com.example.letspaanidriver.activity

import android.content.Intent
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

class Rating : AppCompatActivity() {

    var rating: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.rating)


        star1.setOnClickListener {
            rating = 1
            star1.setImageResource(R.drawable.yellow_star)
            star2.setImageResource(R.drawable.dark_star)
            star3.setImageResource(R.drawable.dark_star)
            star4.setImageResource(R.drawable.dark_star)
            star5.setImageResource(R.drawable.dark_star)
        }

        star2.setOnClickListener {
            rating = 2
            star1.setImageResource(R.drawable.yellow_star)
            star2.setImageResource(R.drawable.yellow_star)
            star3.setImageResource(R.drawable.dark_star)
            star4.setImageResource(R.drawable.dark_star)
            star5.setImageResource(R.drawable.dark_star)
        }
        star3.setOnClickListener {
            rating = 3
            star1.setImageResource(R.drawable.yellow_star)
            star2.setImageResource(R.drawable.yellow_star)
            star3.setImageResource(R.drawable.yellow_star)
            star4.setImageResource(R.drawable.dark_star)
            star5.setImageResource(R.drawable.dark_star)
        }

        star4.setOnClickListener {
            rating = 4
            star1.setImageResource(R.drawable.yellow_star)
            star2.setImageResource(R.drawable.yellow_star)
            star3.setImageResource(R.drawable.yellow_star)
            star4.setImageResource(R.drawable.yellow_star)
            star5.setImageResource(R.drawable.dark_star)
        }
        star5.setOnClickListener {
            rating = 5
            star1.setImageResource(R.drawable.yellow_star)
            star2.setImageResource(R.drawable.yellow_star)
            star3.setImageResource(R.drawable.yellow_star)
            star4.setImageResource(R.drawable.yellow_star)
            star5.setImageResource(R.drawable.yellow_star)
        }


        Submit.setOnClickListener {
            if (rating == 0) {
                Snackbar.make(it, "Please Give Rating To TankerMan", Snackbar.LENGTH_SHORT).show()
            } else {
                GiveRating(rating)


            }
        }

        back.setOnClickListener {
            onBackPressed()
        }
    }

    private fun GiveRating(rating: Int) {
        AlertUtils.showCustomProgressDialog(this@Rating)
        val myapplicationinterface = AppController.instance?.requestQueue?.create(UserService::class.java)
        val call = myapplicationinterface?.getRatingResponse(
            "application/json",
            RatingRequest(
                orderId = FirebaseRecived.OrderID,
                rating = rating,
                userId = UserShared.getUserID(this@Rating)!!
            )
        )

        call?.enqueue(object : Callback<RatingResponse> {
            override fun onFailure(call: Call<RatingResponse>, t: Throwable) {
                AlertUtils.dismissDialog()
                Toast.makeText(this@Rating, "Something is not Right", Toast.LENGTH_SHORT).show()

            }

            override fun onResponse(call: Call<RatingResponse>, response: Response<RatingResponse>) {
                AlertUtils.dismissDialog()
                val ratingResponse = response.body()
                if (ratingResponse?.status!!) {
                    Toast.makeText(this@Rating, ratingResponse.message, Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@Rating, Home::class.java))
                    finishAffinity()
                }


            }
        })


    }
}
