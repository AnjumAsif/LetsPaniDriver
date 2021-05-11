package com.example.letspani.fragments

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.letspaanidriver.R
import com.example.letspani.apis.AppController
import com.example.letspani.apis.UserService
import com.example.letspani.apis.classes.PhoneNumberResponse
import com.example.letspani.apis.classes.SupportRequest
import com.example.letspani.apis.classes.SupportResponse
import com.example.letspani.utils.AlertUtils
import com.example.letspani.utils.UserShared
import kotlinx.android.synthetic.main.support.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


/**
 * Created by Aniket Sharma on 30-Mar-19.
 * as5560811@gmail.com
 */
class Support : Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.support, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Support_Back.setOnClickListener {
            activity?.onBackPressed()
        }

        GetPhoneNumber()

        Send.setOnClickListener {
            if (Reason.text.toString().isNotEmpty() || Message.text.toString().isNotEmpty()) {
                SendQuerryToServer(Reason.text.toString(), Message.text.toString())
            } else {
                Toast.makeText(activity, "Please Fill the Form", Toast.LENGTH_SHORT).show()
            }
        }


    }

    private fun SendQuerryToServer(reason: String, message: String) {
        AlertUtils.showCustomProgressDialog(context!!)
        val myApplicationInterface = AppController.instance?.requestQueue?.create(UserService::class.java)
        val call = myApplicationInterface?.getSupportResponse(
            "application/json",
            SupportRequest(message, reason, UserShared.getUserID(context!!)!!)
        )

        call?.enqueue(object : Callback<SupportResponse> {
            override fun onFailure(call: Call<SupportResponse>, t: Throwable) {
                AlertUtils.dismissDialog()
                Toast.makeText(activity, "Something Went Wrong!!", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<SupportResponse>, response: Response<SupportResponse>) {
                AlertUtils.dismissDialog()
                val supportResponse = response.body()
                if (supportResponse?.status!!) {
                    Toast.makeText(activity, supportResponse.message, Toast.LENGTH_SHORT).show()
                    Reason.text = Editable.Factory.getInstance().newEditable("")
                    Message.text = Editable.Factory.getInstance().newEditable("")
                } else {
                    Toast.makeText(activity, supportResponse.message, Toast.LENGTH_SHORT).show()

                }


            }

        })
    }


    private fun GetPhoneNumber() {
        AlertUtils.showCustomProgressDialog(context!!)
        val myApplicationInterface = AppController.instance?.requestQueue?.create(UserService::class.java)
        val call = myApplicationInterface?.getPhoneNumberResponse("application/json")
        call?.enqueue(object : Callback<PhoneNumberResponse> {
            override fun onFailure(call: Call<PhoneNumberResponse>, t: Throwable) {
                AlertUtils.dismissDialog()
                Toast.makeText(activity, "Something Went Wrong!!", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<PhoneNumberResponse>, response: Response<PhoneNumberResponse>) {
                AlertUtils.dismissDialog()
                val numberResponse = response.body()
                if (numberResponse?.status!!) {
                    number.text = numberResponse.supportPhone.value

                    call_number.setOnClickListener {
                        val callIntent = Intent(Intent.ACTION_CALL)
                        callIntent.data = Uri.parse("tel:${numberResponse.supportPhone.value}")


                        if (ActivityCompat.checkSelfPermission(
                                activity!!,
                                Manifest.permission.CALL_PHONE
                            ) != PackageManager.PERMISSION_GRANTED
                        ) {
                            return@setOnClickListener
                        }
                        startActivity(callIntent)
                    }


                } else {
                    Toast.makeText(activity, numberResponse.message, Toast.LENGTH_SHORT).show()

                }

            }
        })

    }


}