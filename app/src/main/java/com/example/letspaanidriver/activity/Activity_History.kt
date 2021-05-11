package com.example.letspaanidriver.activity
import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.util.Log

import android.widget.CalendarView.OnDateChangeListener
import android.widget.Toast
import com.example.letspaanidriver.R

import com.example.letspaanidriver.apis.classes.CurrentCancelOrderRequest
import com.example.letspaanidriver.apis.classes.CurrentCancelOrderResponse
import com.example.letspaanidriver.apis.classes.DriverOrderHistoryRequest
import com.example.letspaanidriver.apis.classes.DriverOrderHistoryResponse
import com.example.letspani.apis.AppController
import com.example.letspani.apis.UserService
import com.example.letspani.utils.AlertUtils
import com.example.letspani.utils.User
import com.example.letspani.utils.UserShared
import kotlinx.android.synthetic.main.activity_history.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
class Activity_History : AppCompatActivity() {
    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.example.letspaanidriver.R.layout.activity_history)
        val actionbar = supportActionBar
        //set actionbar title
        actionbar!!.title = resources.getString(R.string.history)
        actionbar.setBackgroundDrawable( ColorDrawable(Color.parseColor("#76D4FC")))
        //set back button
        actionbar.setDisplayHomeAsUpEnabled(true)
        actionbar.setDisplayHomeAsUpEnabled(true)
        calendar.setOnDateChangeListener(OnDateChangeListener { view, year, month, dayOfMonth ->
           val curDate = dayOfMonth.toString()
            val y=year
            val m=month
            val d=dayOfMonth

            var mon=m+1


            Log.i("asdf","dateis=asdfsddf===="+y.toString()+"-"+mon.toString()+"-"+d.toString())

            GetOrderHistory(y.toString()+"-"+mon.toString()+"-"+d.toString())
            Log.i("asdf","dateis====="+curDate)
        })


        val c = Calendar.getInstance().time
        val df = SimpleDateFormat("yyyy-MM-dd")
        val formattedDate = df.format(c)
        Log.i("asdf","current dataewe====="+formattedDate)

        GetOrderHistory(formattedDate)

     /*   val today = Date()
        val format = SimpleDateFormat("Y-m-d")
        val dateToStr = format.format(today)*/

      //  Log.i("asdf","current dataewe====="+dateToStr);

    }

    private fun GetOrderHistory(selDate: String) {
        
        totOrderRequest.text = "0"
        totOrderAccept.text = "0"
        totOrderCancelCustomer.text = "0"
        totOrderCancelByDriver.text = "0"
        totDeliveryCompleted.text = "0"
        totActiveHour.text = "0"
        dayEarning.text = "0"


        val myApplicationInterface = AppController.instance?.requestQueue?.create(UserService::class.java)
        val call = myApplicationInterface?.getDriverOrderHistoryResponse(
            "application/json",

            DriverOrderHistoryRequest(UserShared.getUserID(this@Activity_History)!!, selDate)
        )

        call?.enqueue(object : Callback<DriverOrderHistoryResponse> {
            override fun onFailure(call: Call<DriverOrderHistoryResponse>, t: Throwable) {

                Toast.makeText(this@Activity_History, "Something Went Wrong!!", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(
                call: Call<DriverOrderHistoryResponse>,
                response: Response<DriverOrderHistoryResponse>
            ) {

                val currentCancelResponse = response.body()

                Log.i("as","response======="+response.body())

                if(currentCancelResponse!!.status)
                {
                    totOrderRequest.text = currentCancelResponse.history.order_request
                    totOrderAccept.text = currentCancelResponse.history.order_accepted
                    totOrderCancelCustomer.text = currentCancelResponse.history.cancel_byuser
                    totOrderCancelByDriver.text = currentCancelResponse.history.cancel_bydriver
                    totDeliveryCompleted.text = currentCancelResponse.history.delivered_completed
                    totActiveHour.text = currentCancelResponse.history.active_hours
                    dayEarning.text = currentCancelResponse.history.earning
                }
            }
        })
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }


}

