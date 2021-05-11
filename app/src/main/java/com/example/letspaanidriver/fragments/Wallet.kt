package com.example.letspani.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.letspaanidriver.R
import com.example.letspaanidriver.apis.classes.ProfileRequest
import com.example.letspaanidriver.apis.classes.ProfileResponse
import com.example.letspani.apis.AppController
import com.example.letspani.apis.UserService
import com.example.letspani.utils.AlertUtils
import com.example.letspani.utils.UserShared
import kotlinx.android.synthetic.main.wallet.*
import kotlinx.android.synthetic.main.your_orders.back
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


/**
 * Created by Hemant Sharma on 17-May-2020.
 * hkharma4u@gmail.com
 */
class Wallet : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.wallet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        back.setOnClickListener {
            activity!!.onBackPressed()
        }

        getProfileData(1)


        wallet.setOnClickListener {
            cashMoney.text = ""
            getProfileData(1)
            wallet.setTextColor(resources.getColor(R.color.white))
            cash.setTextColor(resources.getColor(R.color.colorAccent))

            wallet.background = resources.getDrawable(R.drawable.order_tab_selected_background)
            cash.background = resources.getDrawable(R.drawable.order_tab_background)

        }
        cash.setOnClickListener {
            cashMoney.text =""
            getProfileData(2)
            cash.setTextColor(resources.getColor(R.color.white))
            wallet.setTextColor(resources.getColor(R.color.colorAccent))

            cash.background = resources.getDrawable(R.drawable.order_tab_selected_background)
            wallet.background = resources.getDrawable(R.drawable.order_tab_background)
        }

    }

    fun getProfileData(type: Int) {
        AlertUtils.showCustomProgressDialog(context!!)
        val userid = UserShared.getUserID(context?.applicationContext!!)
        val myApplicationInterface =
            AppController.instance?.requestQueue?.create(UserService::class.java)
        val call = myApplicationInterface?.getDriverProfile(
            "application/json",
            ProfileRequest(userid.toString())
        )
        call?.enqueue(object : Callback<ProfileResponse> {
            override fun onFailure(call: Call<ProfileResponse>, t: Throwable) {
                AlertUtils.dismissDialog()
            }

            override fun onResponse(
                call: Call<ProfileResponse>,
                response: Response<ProfileResponse>
            ) {
                AlertUtils.dismissDialog()
                val profileResponse = response.body()

                Log.i("tag", "the output is==" + response.body())

                if (profileResponse?.status!!) {
                    if (profileResponse.profileData.wallet != null) {
                        var cashInHand = profileResponse.profileData.wallet.cashInHand
                        var walletMoney = profileResponse.profileData.wallet.walletMoney

                        if (type == 1) {
                            cashMoney.text = "Wallet Money:   " + cashInHand.toString()
                        } else {
                            cashMoney.text = "Cash Money:   " + walletMoney.toString()
                        }
                    }
                }
            }
        })
    }
}