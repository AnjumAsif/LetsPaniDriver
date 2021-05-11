package com.example.letspani.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.example.letspaanidriver.R
import com.example.letspaanidriver.apis.classes.PreferedLocationRequest
import com.example.letspaanidriver.apis.classes.PreferedLocationResponse
import com.example.letspaanidriver.apis.classes.ProfileRequest
import com.example.letspaanidriver.apis.classes.ProfileResponse
import com.example.letspani.apis.AppController
import com.example.letspani.apis.UserService
import com.example.letspani.utils.UserShared
import kotlinx.android.synthetic.main.order_preferences.*
import kotlinx.android.synthetic.main.profile.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


/**
 * Created by Aniket Sharma on 04-May-19.
 * as5560811@gmail.com
 */
class OrderPreferences : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.order_preferences, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getOrderPreferences()

        pref_back.setOnClickListener {
            activity?.onBackPressed()
        }

    }

    fun getOrderPreferences(){

        Log.i("afs","asdfpreferedlocation=======")

        val userid= UserShared.getUserID(context?.applicationContext!!)
        val myApplicationInterface = AppController.instance?.requestQueue?.create(UserService::class.java)
        val call = myApplicationInterface?.getOrderPrefranceResponse("application/json",
            PreferedLocationRequest(userid.toString())
        )
        call?.enqueue(object : Callback<PreferedLocationResponse> {
            override fun onFailure(call: Call<PreferedLocationResponse>, t: Throwable) {
            }

            override fun onResponse(call: Call<PreferedLocationResponse>, response: Response<PreferedLocationResponse>) {
                val profileResponse = response.body()


                for (i in 0..profileResponse!!.locations.size-1) {

                    if(i==0)
                    {
                        locdefault.setText(profileResponse!!.locations[i].location.address)
                        locone.setText(profileResponse!!.locations[i].location.address)
                    }
                    else if(i==1)
                    {
                        loctwo.setText(profileResponse!!.locations[i].location.address)
                    }
                    else if(i==2)
                    {
                        locthree.setText(profileResponse!!.locations[i].location.address)
                    }
                }

            }


        })
    }
}