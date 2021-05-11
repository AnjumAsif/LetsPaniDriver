package com.example.letspaanidriver.fragments


import android.Manifest
import android.app.Activity.RESULT_CANCELED
import android.content.Intent

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import kotlinx.android.synthetic.main.fragment_upload_docs.*

import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat.checkSelfPermission

import androidx.appcompat.app.AlertDialog
import android.widget.Toast
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.widget.ImageView
import com.example.letspaanidriver.R
import com.example.letspaanidriver.apis.classes.*
import com.example.letspaanidriver.utils.ImageLoadTask
import com.example.letspani.apis.AppController
import com.example.letspani.apis.UserService
import com.example.letspani.utils.UserShared
import kotlinx.android.synthetic.main.dailog_gallery_camera.view.*
import org.jetbrains.anko.image
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL


class UploadDocs : Fragment() {
    private var IMAGE_DIRECTORY = "/demonuts"
    private var GALLERY = 1
    private var CAMERA = 2

    private var clickon = 0

    private var endodedString = ""

    private var PRIVATE_MODE = 0
    var document_Type: String? = ""

    var uploadType: String? = ""

    companion object {
        //image pick code
        private val IMAGE_PICK_CODE = 1000
        //Permission code
        private val PERMISSIONC_CODE = 1001

        private val PERMISSIONG_CODE = 1002
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_upload_docs, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        back_image.setOnClickListener {
            activity?.onBackPressed()

        }

        document_Type = DocumentType.dataname
        txttitle.text = document_Type

        uploadType = DocumentType.datatype

        Log.i("asd", "aaaaaaddddd======" + document_Type)


        if (document_Type == "PAN Card" || document_Type == "PassBook" || document_Type == "Vehicle Regn" || document_Type == "Vehicle Image") {
            DownloadImage(image1, uploadType.toString(), "front")
            frontTxt.text = ""
        }

        if (document_Type == "Aadhar Card") {
            uploadImage1.visibility = View.VISIBLE
            DownloadImage(image1, "aadhar_front", "front")
            DownloadImage(image2, "aadhar_back", "back")
        }

        if (document_Type == "Driving License") {
            uploadImage1.visibility = View.VISIBLE
            DownloadImage(image1, "dl_front", "front")
            DownloadImage(image2, "dl_back", "back")
        }

        imgSelectimage1.setOnClickListener {

            if (DocumentType.datatype.equals("aadhar")) {
                uploadType = "aadhar_front"
            }

            if (DocumentType.datatype.equals("dl")) {
                uploadType = "dl_front"
            }
            clickon = 1
            ShowAlertCameraGallery()

        }

        imgSelectimage2.setOnClickListener {
            clickon = 2

            if (DocumentType.datatype.equals("aadhar")) {
                uploadType = "aadhar_back"
            }

            if (DocumentType.datatype.equals("dl")) {
                uploadType = "dl_back"
            }

            ShowAlertCameraGallery()

        }


    }

    //handle requested permission result
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            PERMISSIONC_CODE -> {
                if (grantResults.size > 0 && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED
                ) {

                } else {
                    //permission from popup denied
                    Toast.makeText(context!!, "Permission denied", Toast.LENGTH_SHORT).show()
                }
            }

            PERMISSIONG_CODE -> {
                if (grantResults.size > 0 && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED
                ) {

                } else {
                    //permission from popup denied
                    Toast.makeText(context!!, "Permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_CANCELED) {
            return
        }
        if (requestCode == GALLERY) {
            if (data != null) {
                Toast.makeText(context!!, "Uploaded from Gallery", Toast.LENGTH_SHORT).show()
                val contentURI = data.data

                Log.i("asdf", "imageurigallery" + contentURI)

                try {
                    val bitmap =
                        MediaStore.Images.Media.getBitmap(context!!.contentResolver, contentURI)

                    if (clickon == 1) {
                        image1.setImageBitmap(bitmap)
                        noImage1.visibility = View.GONE


                    } else {
                        image2.setImageBitmap(bitmap)
                        noImage2.visibility = View.GONE

                    }

                    endodedString = encodeToBase64(bitmap)


                    UploadDocumentToServer()


                } catch (e: IOException) {
                    e.printStackTrace()
                    Toast.makeText(context!!, "Failed!", Toast.LENGTH_SHORT).show()
                }

            }

        } else if (requestCode == CAMERA) {
            Toast.makeText(context!!, "Uploaded from Camera", Toast.LENGTH_SHORT).show()
            val thumbnail = data!!.extras!!.get("data") as Bitmap

            Log.i("asdf", "imageuricamera" + data.extras!!.get("data") as Bitmap)
            try {
                if (clickon == 1) {
                    image1.setImageBitmap(thumbnail)
                    noImage1.visibility = View.GONE

                } else {
                    image2.setImageBitmap(thumbnail)
                    noImage2.visibility = View.GONE

                }

                endodedString = encodeToBase64(thumbnail)
                UploadDocumentToServer()
                Toast.makeText(context!!, "Image Saved!", Toast.LENGTH_SHORT).show()
            } catch (e: IOException) {
                e.printStackTrace()
                Toast.makeText(context!!, "Failed!", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun choosePhotoFromGallary() {
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(galleryIntent, GALLERY)
    }

    private fun takePhotoFromCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, CAMERA)
    }


    private fun ShowAlertCameraGallery() {

        val mDialogView =
            LayoutInflater.from(context!!).inflate(R.layout.dailog_gallery_camera, null)
        val mBuilder = AlertDialog.Builder(context!!, R.style.myDialog)
            .setView(mDialogView)

        val mAlertDialog = mBuilder.show()
        //login button click of custom layout
        mDialogView.txtSelectCamera.setOnClickListener {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(context!!, Manifest.permission.CAMERA) ==
                    PackageManager.PERMISSION_DENIED
                ) {
                    //permission denied
                    val permissions = arrayOf(Manifest.permission.CAMERA)
                    //show popup to request runtime permission
                    requestPermissions(permissions, PERMISSIONC_CODE)
                } else {
                    takePhotoFromCamera()

                }
            } else {
                takePhotoFromCamera()

            }
            mAlertDialog.dismiss()


        }
        //cancel button click of custom layout
        mDialogView.txtSelectGallery.setOnClickListener {
            //dismiss dialog
            mAlertDialog.dismiss()

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(context!!, Manifest.permission.READ_EXTERNAL_STORAGE) ==
                    PackageManager.PERMISSION_DENIED
                ) {
                    //permission denied
                    val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
                    //show popup to request runtime permission
                    requestPermissions(permissions, PERMISSIONG_CODE)
                } else {
                    choosePhotoFromGallary()

                }
            } else {
                choosePhotoFromGallary()

            }
        }
    }

    fun encodeToBase64(image: Bitmap): String {
        val immagex = image
        val baos = ByteArrayOutputStream()
        immagex.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val b = baos.toByteArray()
        val imageEncoded = Base64.encodeToString(b, Base64.DEFAULT)
        Log.i("asdf", "LOOKconverttobyte64======" + imageEncoded)
        return imageEncoded
    }


    private fun UploadDocumentToServer() {

        Log.i("asdf", "UploadDocumentToServercall" + uploadType)

        if (!endodedString.equals("")) {
            Log.i("asdf", "notnullencodedstring====" + endodedString)
        }


        val myApplicationInterface =
            AppController.instance?.requestQueue?.create(UserService::class.java)
        val call = myApplicationInterface?.getUploadDocumentResponse(
            "application/json",
            UploadDocumentRequest(
                UserShared.getUserID(context!!).toString(),
                uploadType.toString(),
                endodedString
            )
        )
        call?.enqueue(object : Callback<UploadDocumentResponse> {
            override fun onFailure(call: Call<UploadDocumentResponse>, t: Throwable) {

                Toast.makeText(context!!, "Something Went Wrong!!", Toast.LENGTH_SHORT).show()

            }

            override fun onResponse(
                call: Call<UploadDocumentResponse>,
                response: Response<UploadDocumentResponse>
            ) {
                val uploadDocumentResponse = response.body()
                Log.i("asdf", "ordercomplete====first==" + response.body())

                /* if (uploadDocumentResponse?.status!!) {
                     Toast.makeText(context!!, uploadDocumentResponse.message, Toast.LENGTH_SHORT).show()

                     Log.i("asdf","ordercomplete===secound==="+response.body())



                     //  setupTheMapForDriverLocation(LAT,LONG)

                 } else {
                     Toast.makeText(context!!, uploadDocumentResponse.message, Toast.LENGTH_SHORT).show()
                 }*/

            }
        })

    }


    private fun DownloadImage(image: ImageView, document: String, side: String) {

        val myApplicationInterface =
            AppController.instance?.requestQueue?.create(UserService::class.java)
        val call = myApplicationInterface?.getDriverDocument(
            "application/json",
            DownloadImageRequest(UserShared.getUserID(context!!).toString(), document)
        )
        call?.enqueue(object : Callback<DownloadImageResponse> {
            override fun onFailure(call: Call<DownloadImageResponse>, t: Throwable) {

                Toast.makeText(context!!, "Something Went Wrong!!", Toast.LENGTH_SHORT).show()

            }

            override fun onResponse(
                call: Call<DownloadImageResponse>,
                response: Response<DownloadImageResponse>
            ) {
                val uploadDocumentResponse = response.body()
                Log.i("asdf", "ordercomplete====first==" + response.body())
                if (uploadDocumentResponse != null) {
                    loadingPanel1.visibility = View.GONE
                    loadingPanel2.visibility = View.GONE
                }
                if (uploadDocumentResponse?.document?.documentPath != null) {
                    Log.i(
                        "asdf",
                        "path====first==" + uploadDocumentResponse?.document?.documentPath
                    )
                    ImageLoadTask(uploadDocumentResponse.document.documentPath, image).execute()
                }
                if (uploadDocumentResponse?.status == false && side == "front") {
                    noImage1.visibility = View.VISIBLE
                }
                if (uploadDocumentResponse?.status == false && side == "back") {
                    noImage2.visibility = View.VISIBLE
                }
                Log.i(
                    "asdffas",
                    "checkuserid========" + UserShared.getUserID(activity!!.baseContext)
                )
                /* if (uploadDocumentResponse?.status!!) {
                     Toast.makeText(context!!, uploadDocumentResponse.message, Toast.LENGTH_SHORT).show()
                     Log.i("asdf","ordercomplete===secound==="+response.body())

                 } else {
                     Toast.makeText(context!!, uploadDocumentResponse.message, Toast.LENGTH_SHORT).show()
                     }*/
            }
        })
    }
}