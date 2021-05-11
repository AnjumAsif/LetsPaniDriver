package com.example.letspani.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.letspaanidriver.R
import com.example.letspaanidriver.activity.BasicActivity
import com.example.letspaanidriver.fragments.OrderListAdapter
import com.example.letspani.apis.AppController
import com.example.letspani.apis.UserService
import com.example.letspaanidriver.apis.classes.OrderDetailsRequest
import com.example.letspaanidriver.apis.classes.OrderDetailsResponse
import com.example.letspaanidriver.apis.classes.UserOrderDetailsRequest
import com.example.letspaanidriver.apis.classes.UserOrderDetailsResponse
import com.example.letspani.utils.AlertUtils
import com.example.letspani.utils.UserShared
import kotlinx.android.synthetic.main.order_details.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Created by Aniket Sharma on 06-Jun-19.
 * as5560811@gmail.com
 */
class OrderDetails : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.order_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        back.setOnClickListener {
            activity?.onBackPressed()
        }

        GetOrderDetails(OrderListAdapter.orderId)
        GetuserOrderDetails(OrderListAdapter.orderId)

        support.setOnClickListener {
            val intent = Intent(context, BasicActivity::class.java)
            intent.putExtra("frag", "d")
            startActivity(intent)
        }
    }

    private fun GetOrderDetails(orderId: String?) {
        AlertUtils.showCustomProgressDialog(context!!)
        Log.i("asdf", orderId)
        Log.i("asdf", UserShared.getUserID(context!!)!!)
        val myApplicationInterface =
            AppController.instance?.requestQueue?.create(UserService::class.java)
        val call = myApplicationInterface?.getOrderDetailsResponse(
            "application/json",
            OrderDetailsRequest(orderId!!)
        )
        call?.enqueue(object : Callback<OrderDetailsResponse> {
            override fun onFailure(call: Call<OrderDetailsResponse>, t: Throwable) {
                AlertUtils.dismissDialog()
                Toast.makeText(context, "Something Went Wrong!!", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(
                call: Call<OrderDetailsResponse>,
                response: Response<OrderDetailsResponse>
            ) {
                AlertUtils.dismissDialog()
                val orderDetailsResponse = response.body()
                if (orderDetailsResponse?.status!!) {
                    Toast.makeText(context, orderDetailsResponse.message, Toast.LENGTH_SHORT).show()
                    date_txt.text = orderDetailsResponse.orderDetailss.createdAt
                    orderNumber.text = "Order No. ${orderDetailsResponse.orderDetailss.id}"
                    tanker_type.text =
                        "${orderDetailsResponse.orderDetailss.tankerType} Water Tanker"
                    cencel_bill.text = "₹${orderDetailsResponse.orderDetailss.amount}"
                    bill_amount.text = "₹${orderDetailsResponse.orderDetailss.amount}"
                    round_amount.text = "₹${orderDetailsResponse.orderDetailss.amount}"
                    total_bill_and_tax.text = "₹${orderDetailsResponse.orderDetailss.amount}"
                    ratingsrat.rating =
                        orderDetailsResponse.orderDetailss.driver_rating.toFloat()
                    Log.i("asdf", orderDetailsResponse.orderDetailss.driver_rating.toString())

                    delivery_address.text = orderDetailsResponse.orderDetailss.deliveryAddress


                    when (orderDetailsResponse.orderDetailss.tankerType) {
                        "Small" -> {
                            DetailTankerImage.setImageResource(R.drawable.small)
                        }
                        "Medium" -> {
                            DetailTankerImage.setImageResource(R.drawable.medium)
                        }
                        "Large" -> {
                            DetailTankerImage.setImageResource(R.drawable.large)

                        }
                    }

                } else {
                    Toast.makeText(context, orderDetailsResponse.message, Toast.LENGTH_SHORT).show()

                }
            }
        })
    }

    private fun GetuserOrderDetails(orderId: String?) {
        Log.i("vvbvcvb", orderId)


        val myApplicationInterface =
            AppController.instance?.requestQueue?.create(UserService::class.java)
        val call = myApplicationInterface?.getuserOrderDetailsResponse(
            "application/json",
            UserOrderDetailsRequest(orderId!!, UserShared.getUserID(context!!)!!)
        )
        call?.enqueue(object : Callback<UserOrderDetailsResponse> {
            override fun onFailure(call: Call<UserOrderDetailsResponse>, t: Throwable) {
                AlertUtils.dismissDialog()
                Toast.makeText(context, "Something Went Wrong!!", Toast.LENGTH_SHORT).show()
                Log.i("vvbvcvb", t.message)

            }

            override fun onResponse(
                call: Call<UserOrderDetailsResponse>,
                response: Response<UserOrderDetailsResponse>
            ) {
                AlertUtils.dismissDialog()
                val orderDetailsResponse = response.body()
                if (orderDetailsResponse?.status!!) {
                    user_name.text = orderDetailsResponse.orderDetailss.name
                } else {
                    Toast.makeText(context, orderDetailsResponse.message, Toast.LENGTH_SHORT).show()

                }
            }
        })

    }

}