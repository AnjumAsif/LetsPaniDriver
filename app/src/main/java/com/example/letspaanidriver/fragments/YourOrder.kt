package com.example.letspani.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.letspaanidriver.R
import com.example.letspaanidriver.fragments.OrderListAdapter
import com.example.letspani.apis.AppController
import com.example.letspani.apis.UserService
import com.example.letspaanidriver.apis.classes.OrderListRequest
import com.example.letspaanidriver.apis.classes.OrderListResponse
import com.example.letspani.utils.AlertUtils
import com.example.letspani.utils.UserShared
import kotlinx.android.synthetic.main.your_orders.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


/**
 * Created by Aniket Sharma on 04-May-19.
 * as5560811@gmail.com
 */
class YourOrder : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.your_orders, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        back.setOnClickListener {
            activity!!.onBackPressed()
        }

        GetOrderList("1")


        Current.setOnClickListener {
            GetOrderList("1")
            Current.setTextColor(resources.getColor(R.color.white))
            Delivered.setTextColor(resources.getColor(R.color.colorAccent))
            Cancelled.setTextColor(resources.getColor(R.color.colorAccent))

            Current.background = resources.getDrawable(R.drawable.order_tab_selected_background)
            Delivered.background = resources.getDrawable(R.drawable.order_tab_background)
            Cancelled.background = resources.getDrawable(R.drawable.order_tab_background)

        }
        Delivered.setOnClickListener {
            GetOrderList("3")
            Delivered.setTextColor(resources.getColor(R.color.white))
            Current.setTextColor(resources.getColor(R.color.colorAccent))
            Cancelled.setTextColor(resources.getColor(R.color.colorAccent))

            Delivered.background = resources.getDrawable(R.drawable.order_tab_selected_background)
            Current.background = resources.getDrawable(R.drawable.order_tab_background)
            Cancelled.background = resources.getDrawable(R.drawable.order_tab_background)
        }
        Cancelled.setOnClickListener {
            GetOrderList("4")
            Cancelled.setTextColor(resources.getColor(R.color.white))
            Delivered.setTextColor(resources.getColor(R.color.colorAccent))
            Current.setTextColor(resources.getColor(R.color.colorAccent))

            Cancelled.background = resources.getDrawable(R.drawable.order_tab_selected_background)
            Current.background = resources.getDrawable(R.drawable.order_tab_background)
            Delivered.background = resources.getDrawable(R.drawable.order_tab_background)
        }


    }

    private fun GetOrderList(status: String) {
        Log.i("asdgetUserID", UserShared.getUserID(context!!)!!)

        AlertUtils.showCustomProgressDialog(context!!)

        val myApplicationInterface =
            AppController.instance?.requestQueue?.create(UserService::class.java)
        val call = myApplicationInterface?.getOrderListResponse(
            "application/json",
            OrderListRequest(status, UserShared.getUserID(context!!)!!)
        )

        call?.enqueue(object : Callback<OrderListResponse> {
            override fun onFailure(call: Call<OrderListResponse>, t: Throwable) {
                AlertUtils.dismissDialog()
                Toast.makeText(context, "Something Went Wrong!!", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(
                call: Call<OrderListResponse>,
                response: Response<OrderListResponse>
            ) {
                AlertUtils.dismissDialog()

                val orderListResponse = response.body()
                if (orderListResponse?.status!!) {
                    try {
                        if (!orderListResponse.orders.isNullOrEmpty()) {
                            RecyclerOrderList.visibility = View.VISIBLE
                            RecyclerOrderList.adapter =
                                OrderListAdapter(context!!, orderListResponse.orders)
                            Log.i("asd", "responselist========" + orderListResponse.orders)

                        } else {
                            RecyclerOrderList.visibility = View.GONE
                            Toast.makeText(activity, "Place an Order First", Toast.LENGTH_SHORT).show()

                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                } else {
                    RecyclerOrderList.visibility = View.GONE
                    Toast.makeText(activity, orderListResponse.message, Toast.LENGTH_SHORT).show()
                }
            }
        })


    }
}