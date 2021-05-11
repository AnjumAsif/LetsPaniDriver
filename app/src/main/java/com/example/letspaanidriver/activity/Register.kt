package com.example.letspaanidriver.activity

/*import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode*/

import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.location.Address
import android.location.Geocoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.text.Editable
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.letspaanidriver.R
import com.example.letspaanidriver.apis.classes.RegisterDriverRequest
import com.example.letspaanidriver.apis.classes.RegisterDriverResponse
import com.example.letspani.apis.AppController
import com.example.letspani.apis.UserService
import com.example.letspani.utils.AlertUtils
import com.example.letspani.utils.UserShared
import com.google.android.gms.location.places.ui.PlacePicker
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import kotlinx.android.synthetic.main.activity_register.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class Register : AppCompatActivity() {

    private val PLACE_PICKER_REQUEST = 999
    var LAT: String = ""
    var LANG: String = ""
    private val SYSTEM_ALERT_WINDOW_PERMISSION = 2084

    private var PRIVATE_MODE = 0
    var sharedPrefLanguage: SharedPreferences? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        sharedPrefLanguage = getSharedPreferences("language", PRIVATE_MODE)

        var langval =  sharedPrefLanguage!!.getInt("lang",0)

        if(langval==1)
        {
            val locale = Locale("hi")
            Locale.setDefault(locale)
            val config = Configuration()
            config.locale = locale
            baseContext.resources.updateConfiguration(
                config,
                baseContext.resources.displayMetrics
            )
            setContentView(R.layout.activity_register)
        }
        else{
            setContentView(R.layout.activity_register)
        }
















        tankertype.setOnClickListener {
            val builder = AlertDialog.Builder(this@Register)
            builder.setTitle("Type of Tanker")

            val g = arrayOf("Small", "Medium", "Large")
            builder.setItems(g) { dialog, position ->
                if (position == 0) {
                    tankertype.text = Editable.Factory.getInstance().newEditable(g[position])
                    dialog.cancel()
                }
                if (position == 1) {
                    tankertype.text = Editable.Factory.getInstance().newEditable(g[position])
                    dialog.cancel()
                }
                if (position == 2) {
                    tankertype.text = Editable.Factory.getInstance().newEditable(g[position])
                    dialog.cancel()
                }
            }

            val dialog = builder.create()
            dialog.show()
        }

        register.setOnClickListener {

            val Name = firstame.text.toString()
            val number = moile_no.text.toString()
            val email = email.text.toString()
            val address = address.text.toString()
            val tankertype = tankertype.text.toString()

            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                if (Validation(Name, number, /*email,*/ address, tankertype, it)) {
                    if (convertAddress(address, it)) {
                        SendDataToServer(Name, number, email, address, tankertype)
                    }
                }
            } else if (Settings.canDrawOverlays(this)) {
                if (Validation(Name, number, /*email,*/ address, tankertype, it)) {
                    if (convertAddress(address, it)) {
                        SendDataToServer(Name, number, email, address, tankertype)
                    }
                }
            } else {
                askPermission()
                Toast.makeText(this, "You need System Alert Window Permission to do this", Toast.LENGTH_SHORT).show()
            }





        }

        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.i("asdf", "getInstanceId failed", task.exception)
                    return@OnCompleteListener
                }

                // Get new Instance ID token
                val token = task.result?.token

                UserShared.setFirebaseToken(this@Register, token!!)

                Log.i("asdf", "tokenis============="+ token)


            })




/*        addressText.inputType = InputType.TYPE_NULL
        addressText.isClickable = true*/


        /*     addressText.setOnClickListener {
                 // showLocation()


                 //   NewShowLocation()
             }*/

    }

    private fun askPermission() {
        val intent = Intent(
            Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
            Uri.parse("package:$packageName")
        )
        startActivityForResult(intent, SYSTEM_ALERT_WINDOW_PERMISSION)
    }

    //Code OF Send Data To Server
    private fun SendDataToServer(
        name: String,
        number: String,
        email: String,
        address: String,
        tankertype: String
    ) {
        AlertUtils.showCustomProgressDialog(this@Register)

        Log.i("asdf","Register============="+name)
        Log.i("asdf","Register============="+number)
        Log.i("asdf","Register============="+email)
        Log.i("asdf","Register============="+address)
        Log.i("asdf","Register============="+tankertype)
        Log.i("asdf","Register============="+UserShared.getFirebaseToken(this@Register))

        val myApplicationInterface = AppController.instance?.requestQueue?.create(UserService::class.java)
        val call = myApplicationInterface?.getRegestrationResponse(
            "application/json",
            RegisterDriverRequest(
                address = address, tankerType = tankertype.toLowerCase(), contactNumber = number,
                name = name, email = email, token = UserShared.getFirebaseToken(this@Register)!!
            )
        )

        call?.enqueue(object : Callback<RegisterDriverResponse> {
            override fun onFailure(call: Call<RegisterDriverResponse>, t: Throwable) {

                Log.i("asdf","registercall==============register="+call)
                Log.i("asdf","registert==============register="+t)

                Toast.makeText(applicationContext, "Something Went Wrong!!", Toast.LENGTH_SHORT).show()
                AlertUtils.dismissDialog()

            }

            override fun onResponse(call: Call<RegisterDriverResponse>, response: Response<RegisterDriverResponse>) {
                AlertUtils.dismissDialog()

                val registerResponse = response.body()

                Log.i("asdf","registercall==============register="+call)


                if (registerResponse!!.status) {
                    UserShared.setUserID(this@Register, registerResponse.userId.toString())
                    UserShared.setUserName(this@Register, registerResponse.user_name)

                    Toast.makeText(applicationContext, registerResponse.message, Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@Register, Home::class.java))
                    finish()

                } else {
                    Toast.makeText(applicationContext, registerResponse.message, Toast.LENGTH_SHORT).show()
                }

            }
        })


    }

    //Validation Of Entries
    private fun Validation(
        name: String,
        number: String,
       /* email: String,*/
        address: String,
        tankertype: String,
        view: View
    ): Boolean {
        if (name == "") {
            Snackbar.make(view, "Please Enter Your Name", Snackbar.LENGTH_SHORT).show()
            return false
        }

       /* if (email == "") {
            Snackbar.make(view, "Please Enter Your Email", Snackbar.LENGTH_SHORT).show()
            return false
        }

        if (!email.matches(
                ("[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" + "\\@"
                        + "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" + "(" + "\\."
                        + "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" + ")+").toRegex()
            )
        ) {
            Snackbar.make(view, "Invalid Email Address", Snackbar.LENGTH_SHORT).show()
            return false
        }*/

        if (tankertype == "") {
            Snackbar.make(view, "Please Enter Your Tanker Type", Snackbar.LENGTH_SHORT).show()
            return false
        }
        if (number == "") {
            Snackbar.make(view, "Please Enter Your Number", Snackbar.LENGTH_SHORT).show()
            return false
        }
        if (number.length < 10) {
            Snackbar.make(view, "Please Enter A Valid Number", Snackbar.LENGTH_SHORT).show()
            return false
        }
        if (address == "") {
            Snackbar.make(view, "Please Enter Your Address", Snackbar.LENGTH_SHORT).show()
            return false
        }

        return true
    }

    private fun NewShowLocation() {
        /*    Places.initialize(applicationContext, resources?.getString(R.string.googme_map_key)!!)
         //   val placesClient = Places.createClient(this)
            val fields = Arrays.asList(Place.Field.ID, Place.Field.NAME)


            val intent = Autocomplete.IntentBuilder(
                AutocompleteActivityMode.OVERLAY, fields
            )
                .build(this)
            startActivityForResult(intent, PLACE_PICKER_REQUEST)*/
    }


    private fun showLocation() {


        /*    val builder = PlacePicker.IntentBuilder()
            try {
                // for activty
                startActivityForResult(builder.build(this), PLACE_PICKER_REQUEST)
                // for fragment
                //startActivityForResult(builder.build(getActivity()), PLACE_PICKER_REQUEST);
            } catch (e: GooglePlayServicesRepairableException) {
                e.printStackTrace()
            } catch (e: GooglePlayServicesNotAvailableException) {
                e.printStackTrace()
            }
    */
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        /*     if (requestCode == PLACE_PICKER_REQUEST) {
                 when (resultCode) {
                     Activity.RESULT_OK -> {
                         val place = Autocomplete.getPlaceFromIntent(data!!)
                         Log.v("Aniket", "Place: " + place.name + ", " + place.id)
                         Toast.makeText(applicationContext, place.name,Toast.LENGTH_SHORT).show()

                         addressText.text= Editable.Factory.getInstance().newEditable(place.name)
                     }
                     AutocompleteActivity.RESULT_ERROR -> {
                         // TODO: Handle the error.
                         val status = Autocomplete.getStatusFromIntent(data!!)
                         Log.v("Aniket", status.statusMessage)
                         Toast.makeText(applicationContext, status.statusMessage,Toast.LENGTH_SHORT).show()

                     }
                     Activity.RESULT_CANCELED -> // The user canceled the operation.
                         Toast.makeText(applicationContext,"Canceled",Toast.LENGTH_SHORT).show()
                 }
             }*/


        //  checkPermissionOnActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK) {
            when (requestCode) {
                PLACE_PICKER_REQUEST -> {

                    val place = PlacePicker.getPlace(this, data)
                    val location = place.address.toString()
                    Toast.makeText(this, location, Toast.LENGTH_SHORT).show()
                }


            }
        }

    }


    fun convertAddress(address: String, view: View): Boolean {
        if (!address.isEmpty()) {
            try {
                val geocoder = Geocoder(applicationContext, Locale.getDefault())
                val addressList: List<Address> = geocoder.getFromLocationName(address, 1)
                if (addressList.isNotEmpty()) {
                    LAT = addressList.get(0).latitude.toString()
                    LANG = addressList.get(0).longitude.toString()
                    val locality = addressList.get(0).subLocality
                    Log.v("Location", LAT + " Long=" + LANG + "Locality = " + locality)
                } else {
                    Snackbar.make(view, "Please Enter Valid & Full Address", Snackbar.LENGTH_SHORT).show()
                    return false
                }
            } catch (e: Exception) {

            } // end catch
        } // end if
        return true
    }

}
