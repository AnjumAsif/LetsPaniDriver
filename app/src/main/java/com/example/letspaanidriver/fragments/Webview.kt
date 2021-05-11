package com.example.letspaanidriver.fragments

import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import com.example.letspaanidriver.R
import com.example.letspaanidriver.activity.Home
import kotlinx.android.synthetic.main.webview.*


/**
 * Created by Aniket Sharma on 12-Jun-19.
 * as5560811@gmail.com
 */
class Webview : Fragment() {

    private var PRIVATE_MODE = 0
    var sharedPrefLanguage: SharedPreferences? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var view: View

        view = inflater.inflate(R.layout.webview, container, false)


        return view

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        Web_View.clearFormData()
        Web_View.clearHistory()
        Web_View.clearCache(true)

        Web_View.settings.setAppCacheEnabled(true)
        Web_View.settings.databaseEnabled = true
        Web_View.settings.domStorageEnabled = true
        Web_View.webChromeClient = WebChromeClient()
        Web_View.settings.loadsImagesAutomatically = true
        Web_View.settings.javaScriptEnabled = true
        Web_View.scrollBarStyle = View.SCROLLBARS_INSIDE_OVERLAY

        sharedPrefLanguage = activity!!.getSharedPreferences("language", PRIVATE_MODE)

        var langval = sharedPrefLanguage!!.getInt("lang", 0)

        if (Home.WebViewType == "T") {
            if (langval == 1) {
                txttitle.text = "शर्तें तथा नियम"
                Web_View.loadUrl("http://letspaani.com/terms-conditions-hindi")

            } else {
                Web_View.loadUrl("http://letspaani.com/terms-conditions")
                txttitle.text = "Terms & Conditions"
            }
        } else if (Home.WebViewType == "P") {

            if (langval == 1) {
                txttitle.text = "गोपनीयता नीति"
                Web_View.loadUrl("http://letspaani.com/privacy-policy-hindi")
            } else {
                Web_View.loadUrl("http://letspaani.com/privacy-policy")
                txttitle.text = "Privacy Policy"

            }

        }



        back.setOnClickListener {
            activity!!.onBackPressed()
        }
    }

}