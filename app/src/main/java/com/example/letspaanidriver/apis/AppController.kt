package com.example.letspani.apis

import android.content.Context
import androidx.multidex.MultiDexApplication
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator
import com.nostra13.universalimageloader.core.DisplayImageOptions
import com.nostra13.universalimageloader.core.ImageLoader
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration
import com.nostra13.universalimageloader.core.assist.QueueProcessingType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class AppController : MultiDexApplication() {

    var driverlocationLatitude: Double? = null
    var driverlocationLongitude: Double? = null

    var retrofit: Retrofit? = null




    // initialize the request queue, the queue instance will be created when it is accessed for the first time
    val requestQueue: Retrofit?
        get() {
            if (retrofit == null) {


                val builder = OkHttpClient.Builder()


                val httpLoggingInterceptor = HttpLoggingInterceptor()
                httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
                builder.addInterceptor(httpLoggingInterceptor)
                val okHttpClient = builder.build()

                retrofit = Retrofit.Builder()
                    .baseUrl(ApiUtils.BASE_URL)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()


            }
            return retrofit
        }



    override fun onCreate() {
        super.onCreate()

        instance = this
        initImageLoader(this)

    }

    companion object {

        var instance: AppController? = null

        fun initImageLoader(context: Context) {


            ImageLoaderConfiguration.createDefault(context)

            val opts = DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).build()
            val config = ImageLoaderConfiguration.Builder(context)
            config.threadPriority(Thread.NORM_PRIORITY - 2)
            config.denyCacheImageMultipleSizesInMemory()
            config.defaultDisplayImageOptions(opts)
            config.diskCacheFileNameGenerator(Md5FileNameGenerator())
            config.diskCacheSize(100 * 1024 * 1024) // 50 MiB
            config.tasksProcessingOrder(QueueProcessingType.LIFO)
            config.writeDebugLogs() // Remove for release app

            // Initialize ImageLoader with configuration.
            ImageLoader.getInstance().init(config.build())
        }
    }

}
