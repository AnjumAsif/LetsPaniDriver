package com.example.letspaanidriver.fragments

import android.content.SharedPreferences
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.fragment.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.letspaanidriver.R
import com.example.letspaanidriver.apis.classes.BankDetailUploadRequest
import com.example.letspaanidriver.apis.classes.BankDetailUploadResponse
import com.example.letspaanidriver.apis.classes.DriverRatingRequest
import com.example.letspaanidriver.apis.classes.DriverRatingResponse
import com.example.letspani.apis.AppController
import com.example.letspani.apis.UserService
import com.example.letspani.utils.UserShared
import kotlinx.android.synthetic.main.bank_detail.*
import kotlinx.android.synthetic.main.fragment_upload_docs.*
import kotlinx.android.synthetic.main.order_details.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Bank_Detail : Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.bank_detail, container, false)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val back = view.findViewById<ImageView>(R.id.back) as ImageView

        back.setOnClickListener {
            activity?.onBackPressed()

        }

        submit.setOnClickListener {
            val accountNumber = accountNo.text.toString()
            val ifscCODE = ifscCode.text.toString()
            val bankName = bankName.selectedItem.toString()
            if (Validation(bankName, accountNumber, ifscCODE)) {
                addBankDetails(bankName, accountNumber, ifscCODE)
            }
        }
    }

    private fun addBankDetails(bankName: String, accNumber: String, ifsc: String) {

        val myApplicationInterface = AppController.instance?.requestQueue?.create(UserService::class.java)
        val call = myApplicationInterface?.updateTankermanProfile(
            "applicatiom/json",
            BankDetailUploadRequest(UserShared.getUserID(requireContext())!!.toInt(), bankName, accNumber, ifsc)
        )
        call?.enqueue(object : Callback<BankDetailUploadResponse> {
            override fun onFailure(call: Call<BankDetailUploadResponse>, t: Throwable) {

                Log.i("asdf", "findneworder=======" + t)

                Toast.makeText(requireContext(), "Something Went Wrong!!", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(
                call: Call<BankDetailUploadResponse>,
                response: Response<BankDetailUploadResponse>
            ) {
                val bankUploadResponse = response.body()
                Log.i("asdf", "ratingsis==============" + response.body())
                Toast.makeText(requireContext(), bankUploadResponse!!.message, Toast.LENGTH_SHORT).show()


            }
        })
    }


    private fun Validation(
        bankName: String, accNumber: String, ifsc: String
    ): Boolean {
        if (bankName == "") {
            Snackbar.make(view as View, "Please Select Your Bank Name", Snackbar.LENGTH_SHORT).show()
            return false
        }
        if (accNumber.contains(" ") || accNumber.length > 18 || accNumber.length < 8) {
            Snackbar.make(view as View, "Please Enter a valid Account Number", Snackbar.LENGTH_SHORT).show()
            return false
        }
        if (accNumber == "") {
            Snackbar.make(view as View, "Please Enter Your Account Number", Snackbar.LENGTH_SHORT).show()
            return false
        }
        if (ifsc == "") {
            Snackbar.make(view as View, "Please Enter Your IFSC Code", Snackbar.LENGTH_SHORT).show()
            return false
        }
        if (ifsc.contains(" ") || ifsc.length != 11 || ifsc[4].toString() != "0" || checkStringForAllTheLetters(ifsc)) {
            Snackbar.make(view as View, "Please Enter a valid IFSC Code", Snackbar.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    fun checkStringForAllTheLetters(input:String):Boolean {
        var alpha = false
        for (id in 0 until 3){
            for(no in 0 until 9){
                if(input[id].toString()==no.toString()) {
                    alpha = true
                }
            }
        }
        return alpha
    }
}




