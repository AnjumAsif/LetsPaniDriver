package com.example.letspani.utils

import android.content.Context
import android.content.SharedPreferences


/**
 * Created by Aniket Sharma on 14-May-19.
 * as5560811@gmail.com
 */
object User {

    fun getSharedPreference(context: Context): SharedPreferences =
        context.getSharedPreferences("userinfo", Context.MODE_PRIVATE)
}

object UserShared {

    fun removeUser(context: Context) {
        User.getSharedPreference(context).edit()?.clear()?.apply()

    }

    fun getUserID(context: Context): String? {
        return User.getSharedPreference(context).getString("userID", "")

    }

    fun setUserID(context: Context,userID: String) {
      User.getSharedPreference(context).edit()?.putString("userID", userID)?.apply()

    }
    fun getFirebaseToken(context: Context): String? {
        return User.getSharedPreference(context).getString("firebaseToken", "")

    }

    fun setFirebaseToken(context: Context,firebaseToken: String) {
        User.getSharedPreference(context).edit()?.putString("firebaseToken", firebaseToken)?.apply()
    }
    fun getUserName(context: Context): String? {
        return User.getSharedPreference(context).getString("userName", "")

    }

    fun setUserName(context: Context,userName: String) {
        User.getSharedPreference(context).edit()?.putString("userName", userName)?.apply()
    }

    fun getUserPic(context: Context): String? {
        return User.getSharedPreference(context).getString("userPic", "")

    }

    fun setUserPic(context: Context,userpic: String) {
        User.getSharedPreference(context).edit()?.putString("userPic", userpic)?.apply()
    }
}