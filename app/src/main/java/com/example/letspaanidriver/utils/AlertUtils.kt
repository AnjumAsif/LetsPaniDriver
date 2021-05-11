package com.example.letspani.utils

import android.app.Dialog
import android.content.Context
import android.view.Window
import com.example.letspaanidriver.R
import com.example.letspaanidriver.utils.RotateLoading


/**
 * Created by Aniket Sharma on 04-Jun-19.
 * as5560811@gmail.com
 */
class AlertUtils {



    companion object {

        var dialog: Dialog? = null
        var rotateLoading: RotateLoading? = null
        public fun showCustomProgressDialog(con: Context) {
            // create a Dialog component

            //  DebugLog.e("this is called from home ");
            try {
                dialog = Dialog(con)
                dialog!!.setCancelable(false)
                // this line removes title
                dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
                dialog!!.getWindow()!!.setBackgroundDrawableResource(android.R.color.transparent)
                //tell the Dialog to use the dialog.xml as it's layout description
                dialog!!.setContentView(R.layout.loading_custom_taxiapp)
                rotateLoading = dialog!!.findViewById(R.id.rotateloading) as RotateLoading
                if (dialog != null && !dialog!!.isShowing) {
                    dialog!!.show()
                }
                rotateLoading!!.start()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }// end of showCustomProgressDialog

        public fun dismissDialog() {
            try {
                if (dialog != null) {
                    dialog!!.dismiss()
                    rotateLoading!!.stop()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }
}