package com.example.letspaanidriver.fragments

import android.content.Context
import android.content.Intent
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.letspaanidriver.R
import com.example.letspaanidriver.activity.BasicActivity
import com.example.letspaanidriver.apis.classes.OrderListResponse
import kotlinx.android.synthetic.main.order_list_view.view.*


/**
 * Created by Aniket Sharma on 06-Jun-19.
 * as5560811@gmail.com
 */
class OrderListAdapter(val context: Context, val orderList: List<OrderListResponse.Order>) :
    RecyclerView.Adapter<OrderListAdapter.ViewHolder>() {

    companion object {
        var orderId: String? = null
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {

        val view = LayoutInflater.from(context).inflate(R.layout.order_list_view, p0, false)
        return ViewHolder(view)


    }

    override fun getItemCount(): Int {
        return orderList.size
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {


        val orders = orderList[p1]
        p0.itemView.DateTime.text = orders.updatedAt
        try {
            p0.itemView.OrderListAddress.text = orders.userAddress.address
            p0.itemView.TankerTypeList.text = "${orders.tankerType} Water Tanker"
            p0.itemView.OrderListAmount.text = "₹${orders.amount}"
        } catch (e: Exception) {
           // Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
        }


        when (orders.tankerType) {
            "Small" -> {
                p0.itemView.TankerIcon.setImageResource(R.drawable.small)
            }
            "Medium" -> {
                p0.itemView.TankerIcon.setImageResource(R.drawable.medium)
            }
            "Large" -> {
                p0.itemView.TankerIcon.setImageResource(R.drawable.large)
            }
        }

        p0.itemView.OrderListLayout.setOnClickListener {
            val intent = Intent(context, BasicActivity::class.java)
            intent.putExtra("frag", "g")
            context.startActivity(intent)
            orderId = orders.id.toString()
        }

    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    }
}