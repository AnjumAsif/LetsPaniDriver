package com.example.letspaanidriver.fragments


import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.letspaanidriver.R
import com.example.letspaanidriver.activity.Home
import com.example.letspaanidriver.apis.classes.*
import com.example.letspani.apis.AppController
import com.example.letspani.apis.UserService
import com.example.letspani.utils.UserShared
import kotlinx.android.synthetic.main.dailog_gallery_camera.view.*
import kotlinx.android.synthetic.main.profile.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.io.IOException


class Profile : Fragment() {

    private var GALLERY = 1
    private var CAMERA = 2

    private var endodedString = ""

    internal var permissionsRequired = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.CAMERA
    )

    companion object {
        //image pick code
        private val IMAGE_PICK_CODE = 1000
        //Permission code
        private val PERMISSIONC_CODE = 1001

        private val PERMISSIONG_CODE = 1002
    }

        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
            val view = inflater.inflate(R.layout.profile, container, false)
            val back_image=view.findViewById<ImageView>(R.id.back_image)
            val uploadDoc=view.findViewById<Button>(R.id.uploadDoc) as Button
            val edit_bank_details=view.findViewById<ImageView>(R.id.edit_bank_details) as ImageView
            val changedp=view.findViewById<ImageView>(R.id.changedp) as ImageView
            edit_bank_details.setOnClickListener {
                val fragment = Bank_Detail()
                val fragmentManager = activity?.supportFragmentManager
                val fragmentTransaction = fragmentManager?.beginTransaction()
                fragmentTransaction?.replace(R.id.profile, fragment)
                fragmentTransaction?.addToBackStack(null)
                fragmentTransaction?.commit()
            }

          /*  uploadDoc.setOnClickListener {

                val fragment = UploadDocs()
                val fragmentManager = activity?.supportFragmentManager
                val fragmentTransaction = fragmentManager?.beginTransaction()
                fragmentTransaction?.replace(R.id.profile, fragment)
                fragmentTransaction?.addToBackStack(null)
                fragmentTransaction?.commit()

            }*/

              uploadDoc.setOnClickListener {

               val fragment = SelectDocumentType()
               val fragmentManager = activity?.supportFragmentManager
               val fragmentTransaction = fragmentManager?.beginTransaction()
               fragmentTransaction?.replace(R.id.profile, fragment)
               fragmentTransaction?.addToBackStack(null)
               fragmentTransaction?.commit()

           }

            back_image.setOnClickListener {
                if (Fragment() != null) {
                    val intent = Intent(context, Home::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                    startActivity(intent)
                    //activity?.finish()

                }
            }


            changedp.setOnClickListener {
                ShowAlertCameraGallery()
            }



            getProfileData()
            getDriverRatingData()


            return view

        }


    fun getProfileData(){
        val userid=UserShared.getUserID(context?.applicationContext!!)
        val myApplicationInterface = AppController.instance?.requestQueue?.create(UserService::class.java)
        val call = myApplicationInterface?.getDriverProfile("application/json",
            ProfileRequest(userid.toString()))
        call?.enqueue(object : Callback<ProfileResponse>{
            override fun onFailure(call: Call<ProfileResponse>, t: Throwable) {
            }

            override fun onResponse(call: Call<ProfileResponse>, response: Response<ProfileResponse>) {
                if (response.body() != null) {
                    val profileResponse = response.body()

                    Log.i("tag", "the output is==" + response.body())

                    try {
                        if (profileResponse?.status!!) {
                            // id_value.setText(profileResponse!!.profileData.id)
                            phone_no_value.setText(profileResponse!!.profileData.contactNumber)
                            profile_name.setText(profileResponse!!.profileData.name)
                            if (profileResponse!!.profileData.vehicles != null) {
                                vehicle_value.setText(profileResponse!!.profileData.vehicles.vehicleNo)
                            }
                            dl_no_value.setText(profileResponse!!.profileData.tankermanDetails.licenceId)
                            adhar_value.setText(profileResponse!!.profileData.tankermanDetails.aadharNumber)
                            dl_expiry_value.setText(profileResponse!!.profileData.tankermanDetails.licenceExpiryDate)

                            bank_name_value.setText(profileResponse!!.profileData.tankermanDetails.bankName)
                            account_no_value.setText(profileResponse!!.profileData.tankermanDetails.accountNumber)
                            ifsc_code_value.setText(profileResponse!!.profileData.tankermanDetails.ifscCode)
                            id_value.setText(userid.toString())



                            if (UserShared.getUserPic(context?.applicationContext!!)!!.isNotEmpty()) {
                                Glide.with(context?.applicationContext!!)
                                    .load(UserShared.getUserPic(context?.applicationContext!!))
                                    .apply(RequestOptions.skipMemoryCacheOf(true))
                                    .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
                                    .into(profile_pic);
                            } else {
                                profile_pic.setImageResource(R.drawable.default_pic)
                            }


                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }


        })
    }


    private fun ShowAlertCameraGallery() {

        val mDialogView = LayoutInflater.from(context!!).inflate(R.layout.dailog_gallery_camera, null)
        val mBuilder = AlertDialog.Builder(context!!, R.style.myDialog)
            .setView(mDialogView)

        val mAlertDialog = mBuilder.show()
        //login button click of custom layout
        mDialogView.txtSelectCamera.setOnClickListener {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ContextCompat.checkSelfPermission(context!!, Manifest.permission.CAMERA) ==
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
                if (ContextCompat.checkSelfPermission(context!!, Manifest.permission.READ_EXTERNAL_STORAGE) ==
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


    private fun choosePhotoFromGallary() {
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(galleryIntent, GALLERY)
    }

    private fun takePhotoFromCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, CAMERA)
    }


    //handle requested permission result
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
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
        if (resultCode == Activity.RESULT_CANCELED) {
            return
        }
        if (requestCode == GALLERY) {
            if (data != null) {
                Toast.makeText(context!!, "Uploaded from Gallery", Toast.LENGTH_SHORT).show()
                val contentURI = data.data

                Log.i("asdf", "imageurigallery" + contentURI)

                try {
                    val bitmap = MediaStore.Images.Media.getBitmap(context!!.contentResolver, contentURI)
                    profile_pic.setImageBitmap(bitmap)

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

                profile_pic.setImageBitmap(thumbnail)


                endodedString = encodeToBase64(thumbnail)
                UploadDocumentToServer()
                Toast.makeText(context!!, "Image Saved!", Toast.LENGTH_SHORT).show()
            } catch (e: IOException) {
                e.printStackTrace()
                Toast.makeText(context!!, "Failed!", Toast.LENGTH_SHORT).show()
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

        if (!endodedString.equals("")) {
            Log.i("asdf", "notnullencodedstring====" + endodedString)
        }


        val myApplicationInterface = AppController.instance?.requestQueue?.create(UserService::class.java)
        val call = myApplicationInterface?.getUploadUserProfileResponse(
            "application/json",
            UploadUserProfileRequest(UserShared.getUserID(context!!).toString(), endodedString)
        )
        call?.enqueue(object : Callback<UploadProfileResponse> {
            override fun onFailure(call: Call<UploadProfileResponse>, t: Throwable) {

                Toast.makeText(context!!, "Something Went Wrong!!", Toast.LENGTH_SHORT).show()

            }

            override fun onResponse(call: Call<UploadProfileResponse>, response: Response<UploadProfileResponse>) {
                val uploadDocumentResponse = response.body()
                Log.i("asdf", "ordercomplete====first==" + response.body())

                 if (uploadDocumentResponse?.status!!) {
                     Toast.makeText(context!!, uploadDocumentResponse.message, Toast.LENGTH_SHORT).show()

                     Log.i("asdf","ordercomplete===secound==="+response.body())


                     val result = Intent()
                     getActivity()!!.setResult(Activity.RESULT_OK, result)

                     getProfileData()

                     //  setupTheMapForDriverLocation(LAT,LONG)

                 } else {
                     Toast.makeText(context!!, uploadDocumentResponse.message, Toast.LENGTH_SHORT).show()
                 }

            }
        })

    }


    fun getDriverRatingData() {
        val userid = UserShared.getUserID(context?.applicationContext!!)!!
        val myApplicationInterface = AppController.instance?.requestQueue?.create(UserService::class.java)
        val call = myApplicationInterface?.getUserRating(
            "application/json",
            DriverRatingRequest(userid)
        )
        call?.enqueue(object : Callback<DriverRatingResponse> {
            override fun onFailure(call: Call<DriverRatingResponse>, t: Throwable) {
            }

            override fun onResponse(call: Call<DriverRatingResponse>, response: Response<DriverRatingResponse>) {
                val driverResponse = response.body()

                Log.i("asd", "response======" + response.body())
                if (driverResponse?.allRatings != null) {
                    ratingProfile.rating = driverResponse.rating.toFloat()
                }
            }
        })
    }


    override fun onResume() {
        super.onResume()
        if (UserShared.getUserPic(context!!)!!.isNotEmpty()) {

            Glide.with(context!!).load(UserShared.getUserPic(context!!)!!).into(profile_pic);


        } else {
            profile_pic.setImageResource(R.drawable.default_pic)
        }
    }




}


