package com.example.letspaanidriver.fragments

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.letspaanidriver.R
import com.example.letspaanidriver.activity.BasicActivity
import kotlinx.android.synthetic.main.fragment_upload_docs.*
import kotlinx.android.synthetic.main.fragment_upload_docs.back_image
import com.example.letspaanidriver.apis.classes.DocumentType
import kotlinx.android.synthetic.main.fragment_selectdocumenttype.*

class SelectDocumentType : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_selectdocumenttype, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


      /*  txtProfilephoto.setOnClickListener {
            fragTransaction("Profile Photo","profile")

        }*/
        txtPANCard.setOnClickListener {
            fragTransaction("PAN Card","pan_card")
        }
        txtBankPassbook.setOnClickListener {
            fragTransaction("PassBook","bank_passbook")
        }
        txtVehicleRegistration.setOnClickListener {
            fragTransaction("Vehicle Registration","vehicle_registration")
        }
        txtVehicleImage.setOnClickListener {
            fragTransaction("Vehicle Image","vehicle_image")
        }
        txtAadharCard.setOnClickListener {
            fragTransaction("Aadhar Card","aadhar")
        }
        txtDrivingLicense.setOnClickListener {
            fragTransaction("Driving License","dl")
        }

        back_image.setOnClickListener {
            activity?.onBackPressed()
        }

    }


//TODO: send input from this fragment to next for document type.

    fun fragTransaction(docname: String,type: String) {
        DocumentType.dataname = docname
        DocumentType.datatype = type

        val intent = Intent(requireContext(), BasicActivity::class.java)
        intent.putExtra("frag", "i")
        startActivity(intent)
    }
}