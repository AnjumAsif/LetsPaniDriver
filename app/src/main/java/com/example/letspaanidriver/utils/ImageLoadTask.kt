package com.example.letspaanidriver.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.widget.ImageView
import java.net.HttpURLConnection
import java.net.URL


class ImageLoadTask(private val url: String, private val imageView: ImageView) : AsyncTask<Void, Void, Bitmap>() {

    override fun doInBackground(vararg params: Void): Bitmap? {
        try {
            val urlConnection = URL(url)
            val connection = urlConnection
                .openConnection() as HttpURLConnection
            connection.doInput = true
            connection.connect()
            val input = connection.inputStream
            return BitmapFactory.decodeStream(input)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return null
    }

    override fun onPostExecute(result: Bitmap) {
        super.onPostExecute(result)
        imageView.setImageBitmap(result)
    }

}